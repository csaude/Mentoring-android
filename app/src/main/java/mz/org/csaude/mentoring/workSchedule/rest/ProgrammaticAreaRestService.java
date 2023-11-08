package mz.org.csaude.mentoring.workSchedule.rest;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseRestService;
import mz.org.csaude.mentoring.dto.form.FormDTO;
import mz.org.csaude.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.csaude.mentoring.dto.tutor.TutorDTO;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.model.form.Form;
import mz.org.csaude.mentoring.model.programmaticArea.ProgrammaticArea;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.service.ProgrammaticArea.ProgrammaticAreaService;
import mz.org.csaude.mentoring.service.ProgrammaticArea.ProgrammaticAreaServiceImpl;
import mz.org.csaude.mentoring.service.form.FormService;
import mz.org.csaude.mentoring.service.form.FormServiceImpl;
import mz.org.csaude.mentoring.service.metadata.LoadMetadataServiceImpl;
import mz.org.csaude.mentoring.service.tutor.TutorService;
import mz.org.csaude.mentoring.service.tutor.TutorServiceImpl;
import mz.org.csaude.mentoring.util.Utilities;
import mz.org.csaude.mentoring.workSchedule.work.FormWorker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgrammaticAreaRestService extends BaseRestService {

    private TutorService tutorService;
    public ProgrammaticAreaRestService(Application application, User currentUser) {
        super(application, currentUser);
        this.tutorService = new TutorServiceImpl(application, currentUser);
    }
    public ProgrammaticAreaRestService(Application application) {
        super(application);
    }

    public void restGetProgrammaticArea(long offset, long limit, RestResponseListener<ProgrammaticArea> listener){

        Call<List<ProgrammaticAreaDTO>> programmaticAreaCall = syncDataService.getProgrammaticAreas(limit, offset);

        programmaticAreaCall.enqueue(new Callback<List<ProgrammaticAreaDTO>>() {
            @Override
            public void onResponse(Call<List<ProgrammaticAreaDTO>> call, Response<List<ProgrammaticAreaDTO>> response) {

                List<ProgrammaticAreaDTO> data = response.body();
                if(!Utilities.listHasElements(data)){
                    listener.doOnResponse(REQUEST_NO_DATA, null);
                }else {

                    try {
                        ProgrammaticAreaService programmaticAreaService = new ProgrammaticAreaServiceImpl(LoadMetadataServiceImpl.APP);
                        Toast.makeText(APP.getApplicationContext(), "Carregando as PROGRAMMATICSAREA...", Toast.LENGTH_SHORT).show();
                        programmaticAreaService.saveOrUpdateProgrammaticAreas(data);

                        List<ProgrammaticArea> programmaticAreas = new ArrayList<>();

                        for(ProgrammaticAreaDTO programmaticAreaDTO : data){
                            programmaticAreas.add(new ProgrammaticArea(programmaticAreaDTO));
                        }

                        listener.doOnResponse(BaseRestService.REQUEST_SUCESS, programmaticAreas);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(APP.getApplicationContext(), "PROGRAMMATICSAREA CARREGADAS COM SUCESSO!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<ProgrammaticAreaDTO>> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar os PROGRAMMATICSAREA. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });

    }

    public void restGetProgrammaticAreaByTutorUuid(RestResponseListener<ProgrammaticArea> listener){

        Tutor tutor ;

        try {
            tutor = this.tutorService.getTutorByUser(currentUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Call<ProgrammaticAreaDTO> programmaticAreaCall = syncDataService.getProgrammaticAreaByTutorUuid(tutor.getUuid());

        programmaticAreaCall.enqueue(new Callback<ProgrammaticAreaDTO>() {
            @Override
            public void onResponse(Call<ProgrammaticAreaDTO> call, Response<ProgrammaticAreaDTO> response) {

                List<ProgrammaticAreaDTO> datas = new ArrayList<>();


                ProgrammaticAreaDTO data = response.body();

                datas.add(data);
                if(!Utilities.listHasElements(datas)){
                    listener.doOnResponse(REQUEST_NO_DATA, null);
                }else {

                    try {
                        ProgrammaticAreaService programmaticAreaService = new ProgrammaticAreaServiceImpl(LoadMetadataServiceImpl.APP);
                        Toast.makeText(APP.getApplicationContext(), "Carregando as PROGRAMMATICSAREA...", Toast.LENGTH_SHORT).show();
                        programmaticAreaService.saveOrUpdateProgrammaticAreas(datas);

                        createForms(data.getUuid(),  listener);

                        listener.doOnResponse(BaseRestService.REQUEST_SUCESS, Collections.singletonList(new ProgrammaticArea(data)));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(APP.getApplicationContext(), "PROGRAMMATICSAREA CARREGADAS COM SUCESSO!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ProgrammaticAreaDTO> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar os PROGRAMMATICSAREA. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });

    }

    private void createForms(String programmaticAreaUuid, RestResponseListener<ProgrammaticArea> listener){

       Call<List<FormDTO>> formDTOCall = syncDataService.getFormByProgrammaticAreaUuid(programmaticAreaUuid);
        formDTOCall.enqueue(new Callback<List<FormDTO>>() {
            @Override
            public void onResponse(Call<List<FormDTO>> call, Response<List<FormDTO>> response) {

                List<FormDTO> data = response.body();

                if(!Utilities.listHasElements(data)){
                    listener.doOnResponse(REQUEST_NO_DATA, null);
                } else {

                    try {
                        FormService formService = new FormServiceImpl(LoadMetadataServiceImpl.APP);
                        Toast.makeText(APP.getApplicationContext(), "Carregando as FORMS...", Toast.LENGTH_SHORT).show();
                        formService.savedOrUpdateForms(data);

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    Toast.makeText(APP.getApplicationContext(), "FORMS CARREGADAS COM SUCESSO!", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<List<FormDTO>> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar os TIPOS DE FORMS. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });

    }

}
