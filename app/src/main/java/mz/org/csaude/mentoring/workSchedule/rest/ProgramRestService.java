package mz.org.csaude.mentoring.workSchedule.rest;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseRestService;
import mz.org.csaude.mentoring.dto.program.ProgramDTO;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.model.program.Program;
import mz.org.csaude.mentoring.service.metadata.LoadMetadataServiceImpl;
import mz.org.csaude.mentoring.service.program.ProgramService;
import mz.org.csaude.mentoring.service.program.ProgramServiceImpl;
import mz.org.csaude.mentoring.util.Utilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgramRestService extends BaseRestService {
    public ProgramRestService(Application application) {
        super(application);
    }

    public void restGetPrograms(RestResponseListener<Program> listener){

        Call<List<ProgramDTO>> programsCall = syncDataService.getAllPrograms();

        programsCall.enqueue(new Callback<List<ProgramDTO>>() {
            @Override
            public void onResponse(Call<List<ProgramDTO>> call, Response<List<ProgramDTO>> response) {

                List<ProgramDTO> data = response.body();

                if(Utilities.listHasElements(data)){
                    try {
                        ProgramService programService = new ProgramServiceImpl(LoadMetadataServiceImpl.APP);
                        Toast.makeText(APP.getApplicationContext(), "Carregando os Programas.", Toast.LENGTH_SHORT).show();
                        List<Program> programs = new ArrayList<>();
                        for (ProgramDTO programDTO : data){
                            programs.add(programDTO.getProgram());
                        }
                        programService.saveOrUpdatePrograms(data);
                        listener.doOnResponse(BaseRestService.REQUEST_SUCESS, programs);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(APP.getApplicationContext(), "PROGRAMAS CARREGADOS COM SUCESSO", Toast.LENGTH_SHORT).show();
                } else {
                    listener.doOnResponse(REQUEST_NO_DATA, null);
                }
            }

            @Override
            public void onFailure(Call<List<ProgramDTO>> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar os Programas. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });

    }

}
