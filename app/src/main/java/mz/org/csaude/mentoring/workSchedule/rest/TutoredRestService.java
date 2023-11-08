package mz.org.csaude.mentoring.workSchedule.rest;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseRestService;
import mz.org.csaude.mentoring.dto.tutor.TutorDTO;
import mz.org.csaude.mentoring.dto.tutored.TutoredDTO;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.tutored.Tutored;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.service.metadata.LoadMetadataServiceImpl;
import mz.org.csaude.mentoring.service.tutor.TutorService;
import mz.org.csaude.mentoring.service.tutor.TutorServiceImpl;
import mz.org.csaude.mentoring.service.tutored.TutoredService;
import mz.org.csaude.mentoring.service.tutored.TutoredServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutoredRestService extends BaseRestService {

    private TutorService tutorService;
    public TutoredRestService(Application application, User currentUser) {
        super(application, currentUser);
        this.tutorService = new TutorServiceImpl(application, currentUser);
    }

    public  void restGetTutored(long offSet, long limit, RestResponseListener<Tutored> listener){

        Call<List<TutoredDTO>> tutoredCall = syncDataService.getTutoreds( limit, offSet);

        tutoredCall.enqueue(new Callback<List<TutoredDTO>>() {
            @Override
            public void onResponse(Call<List<TutoredDTO>> call, Response<List<TutoredDTO>> response) {

                List<TutoredDTO> data = response.body();

                if(data == null){
                    //
                }
                try {
                TutoredService tutoredService = new TutoredServiceImpl(LoadMetadataServiceImpl.APP, currentUser);
                Toast.makeText(APP.getApplicationContext(), "Carregando os Tutorados...", Toast.LENGTH_SHORT).show();
                tutoredService.savedOrUpdateTutoreds(data);
                List<Tutored> tutoreds = new ArrayList<>();
                for (TutoredDTO tutoredDTO : data){
                    tutoreds.add(new Tutored(tutoredDTO));
                }
                listener.doOnResponse(BaseRestService.REQUEST_SUCESS, tutoreds);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(APP.getApplicationContext(), "Tutoreds Carregados com sucessos", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<TutoredDTO>> call, Throwable t) {

                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar os Tutoreds. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });

    }

    public void restGetTutoredByUserUuid( RestResponseListener<Tutored> listener){
        Response<TutorDTO> tutorDTOResponse;
        Tutor tutor;
        try {
             tutor = this.tutorService.getTutorByUser(currentUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Call<List<TutoredDTO>> tutoredCall = syncDataService.getTutoredsByUserUuid(tutor.getUuid());

        tutoredCall.enqueue(new Callback<List<TutoredDTO>>() {
            @Override
            public void onResponse(Call<List<TutoredDTO>> call, Response<List<TutoredDTO>> response) {

                List<TutoredDTO> data = response.body();

                if(data == null){
                    //
                }
                try {
                    TutoredService tutoredService = new TutoredServiceImpl(LoadMetadataServiceImpl.APP);
                    Toast.makeText(APP.getApplicationContext(), "Carregando os Tutorados...", Toast.LENGTH_SHORT).show();
                    tutoredService.savedOrUpdateTutoreds(data);
                    List<Tutored> tutoreds = new ArrayList<>();
                    for (TutoredDTO tutoredDTO : data){
                        tutoreds.add(new Tutored(tutoredDTO));
                    }
                    listener.doOnResponse(BaseRestService.REQUEST_SUCESS, tutoreds);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(APP.getApplicationContext(), "Tutoreds Carregados com sucessos", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<TutoredDTO>> call, Throwable t) {

                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar os Tutoreds. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });



    }

}
