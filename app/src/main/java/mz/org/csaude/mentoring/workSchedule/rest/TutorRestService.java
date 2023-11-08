package mz.org.csaude.mentoring.workSchedule.rest;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseRestService;
import mz.org.csaude.mentoring.dto.career.CareerDTO;
import mz.org.csaude.mentoring.dto.tutor.TutorDTO;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.service.metadata.LoadMetadataServiceImpl;
import mz.org.csaude.mentoring.service.tutor.TutorService;
import mz.org.csaude.mentoring.service.tutor.TutorServiceImpl;
import mz.org.csaude.mentoring.service.tutored.TutoredService;
import mz.org.csaude.mentoring.service.tutored.TutoredServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutorRestService extends BaseRestService {


    public TutorRestService(Application application, User currentUser) {
        super(application, currentUser);
    }

    public void restGetTutor(long offset, long limit, RestResponseListener<Tutor> listener){

        Call<List<TutorDTO>> tutorCall = syncDataService.getTutors(limit, offset);

        tutorCall.enqueue(new Callback<List<TutorDTO>>() {
            @Override
            public void onResponse(Call<List<TutorDTO>> call, Response<List<TutorDTO>> response) {

                List<TutorDTO> data = response.body();

                if(data == null){

                }
                try {

                TutorService tutorService = new TutorServiceImpl(LoadMetadataServiceImpl.APP, currentUser);
                Toast.makeText(APP.getApplicationContext(), "Carregando os Tutores", Toast.LENGTH_SHORT).show();
                tutorService.saveOrUpdateTutors(data);

                List<Tutor> tutors = new ArrayList<>();
                for (TutorDTO tutorDTO : data){
                    tutors.add(new Tutor(tutorDTO));
                }
                listener.doOnResponse(BaseRestService.REQUEST_SUCESS, tutors);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(APP.getApplicationContext(), "Tutores carregadas com sucesso!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<TutorDTO>> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar os Tutores. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);

            }
        });

    }


    public void restGetTutoredUserUuid(RestResponseListener<Tutor> listener){

        Call<TutorDTO> tutorCall = syncDataService.getTutorByUserUuid(currentUser.getUuid());

        tutorCall.enqueue(new Callback<TutorDTO>() {
            @Override
            public void onResponse(Call<TutorDTO> call, Response<TutorDTO> response) {
              TutorDTO  data = response.body();
              List<TutorDTO> tutorDTOS = new ArrayList<>();
                List<Tutor> tutors = new ArrayList<>();


                if(data == null){

                }

                try {

                TutorService tutorService = new TutorServiceImpl(LoadMetadataServiceImpl.APP, currentUser);
                Toast.makeText(APP.getApplicationContext(), "Carregando os Tutores", Toast.LENGTH_SHORT).show();
                tutorDTOS.add(data);
                tutorService.saveOrUpdateTutors(tutorDTOS);

                listener.doOnResponse(BaseRestService.REQUEST_SUCESS, Collections.singletonList(new Tutor(tutorDTOS.get(0))));

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(APP.getApplicationContext(), "Tutor carregadas com sucesso!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<TutorDTO> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar os Tutores. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });
    }
}
