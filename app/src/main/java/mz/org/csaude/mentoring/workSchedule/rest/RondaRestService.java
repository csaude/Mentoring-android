package mz.org.csaude.mentoring.workSchedule.rest;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseRestService;
import mz.org.csaude.mentoring.common.HttpStatus;
import mz.org.csaude.mentoring.common.MentoringAPIError;
import mz.org.csaude.mentoring.dto.ronda.RondaDTO;
import mz.org.csaude.mentoring.dto.tutored.TutoredDTO;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.model.ronda.Ronda;
import mz.org.csaude.mentoring.model.tutor.Tutor;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.service.ronda.RondaService;
import mz.org.csaude.mentoring.service.ronda.RondaServiceImpl;
import mz.org.csaude.mentoring.util.SyncSatus;
import mz.org.csaude.mentoring.util.Utilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RondaRestService extends BaseRestService {

    public RondaRestService(Application application) {
        super(application);
    }

    public void restGetRondas(RestResponseListener<Ronda> listener){
        Tutor mentor = getApplication().getCurrMentor();
        if(mentor==null) {
            try {
                User user =  getApplication().getUserService().getCurrentUser();
                if(user==null) return;
                mentor = getApplication().getTutorService().getByEmployee(user.getEmployee());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        Call<List<RondaDTO>> rondasCall = syncDataService.getAllOfMentor(mentor.getUuid());

        rondasCall.enqueue(new Callback<List<RondaDTO>>() {
            @Override
            public void onResponse(Call<List<RondaDTO>> call, Response<List<RondaDTO>> response) {
                List<RondaDTO> data = response.body();
                if (Utilities.listHasElements(data)) {
                    try {
                        RondaService rondaService = getApplication().getRondaService();
                        List<Ronda> rondas = new ArrayList<>();

                        for (RondaDTO rondaDTO: data) {
                            Ronda ronda = new Ronda(rondaDTO);
                            ronda.setSyncStatus(SyncSatus.SENT);
                            rondaDTO.getHealthFacility().getHealthFacilityObj().setSyncStatus(SyncSatus.SENT);
                            rondas.add(ronda);
                        }
                        Toast.makeText(APP.getApplicationContext(), "Carregando as Rondas.", Toast.LENGTH_SHORT).show();
                        rondaService.saveOrUpdateRondas(data);

                        listener.doOnResponse(BaseRestService.REQUEST_SUCESS, rondas);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(APP.getApplicationContext(), "RONDAS CARREGADAS COM SUCESSO", Toast.LENGTH_SHORT).show();
                } else {
                    listener.doOnResponse(BaseRestService.REQUEST_NO_DATA, null);
                }
            }

            @Override
            public void onFailure(Call<List<RondaDTO>> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar as Rondas. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });
    }

    public void restPostRondas(RestResponseListener<Ronda> listener){

        List<Ronda> rondas = null;
        try {
            rondas = getApplication().getRondaService().getAllNotSynced();
            if (Utilities.listHasElements(rondas)) {
                Call<List<RondaDTO>> rondaCall = syncDataService.postRondas(Utilities.parse(rondas, RondaDTO.class));
                rondaCall.enqueue(new Callback<List<RondaDTO>>() {
                    @Override
                    public void onResponse(Call<List<RondaDTO>> call, Response<List<RondaDTO>> response) {
                        List<RondaDTO> data = response.body();
                        if (response.code() == 201) {
                            try {
                                List<Ronda> rondaList = getApplication().getRondaService().getAllNotSynced();
                                for (Ronda ronda : rondaList) {
                                    ronda.setSyncStatus(SyncSatus.SENT);
                                    getApplication().getRondaService().update(ronda);
                                }

                                listener.doOnResponse(BaseRestService.REQUEST_SUCESS, rondaList);
                            } catch (SQLException  e) {
                                throw new RuntimeException(e);
                            }
                        } else listener.doOnRestErrorResponse(response.message());
                    }

                    @Override
                    public void onFailure(Call<List<RondaDTO>> call, Throwable t) {
                        Log.i("METADATA LOAD --", t.getMessage(), t);
                    }
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void restPostRonda(Ronda ronda, RestResponseListener<Ronda> listener){

        RondaDTO dto = new RondaDTO(ronda);
        Call<RondaDTO> rondaCall = syncDataService.postRonda(dto);
        rondaCall.enqueue(new Callback<RondaDTO>() {
            @Override
            public void onResponse(Call<RondaDTO> call, Response<RondaDTO> response) {
                RondaDTO data = response.body();
                if (response.code() == 201) {
                    try {
                        getApplication().getRondaService().savedOrUpdateRonda(ronda);

                        listener.doOnResponse(BaseRestService.REQUEST_SUCESS, Utilities.parseToList(ronda));
                    } catch (SQLException  e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    if (response.code() == HttpStatus.BAD_REQUEST) {
                        // Parse custom error response
                        try {
                            Gson gson = new Gson();
                            MentoringAPIError error = gson.fromJson(response.errorBody().string(), MentoringAPIError.class);
                            listener.doOnRestErrorResponse(error.getMessage());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        // Handle other error responses
                        listener.doOnRestErrorResponse(response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<RondaDTO> call, Throwable t) {
                Log.i("METADATA LOAD --", t.getMessage(), t);
                listener.doOnRestErrorResponse(t.getMessage());
            }
        });
    }
}
