package mz.org.csaude.mentoring.workSchedule.rest;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseRestService;
import mz.org.csaude.mentoring.dto.mentorship.IterationTypeDTO;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.model.mentorship.IterationType;
import mz.org.csaude.mentoring.service.mentorship.IterationTypeService;
import mz.org.csaude.mentoring.service.mentorship.IterationTypeServiceImpl;
import mz.org.csaude.mentoring.service.metadata.LoadMetadataServiceImpl;
import mz.org.csaude.mentoring.util.SyncSatus;
import mz.org.csaude.mentoring.util.Utilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IterationTypeRestService extends BaseRestService {
    public IterationTypeRestService(Application application) {
        super(application);
    }

    public void restGetIterationTypes(RestResponseListener<IterationType> listener){

        Call<List<IterationTypeDTO>> iterationTypesCall = syncDataService.getIterationTypes();

        iterationTypesCall.enqueue(new Callback<List<IterationTypeDTO>>() {
            @Override
            public void onResponse(Call<List<IterationTypeDTO>> call, Response<List<IterationTypeDTO>> response) {

                List<IterationTypeDTO> data = response.body();

                if(Utilities.listHasElements(data)){
                    try {
                        IterationTypeService evaluationTypeService = new IterationTypeServiceImpl(LoadMetadataServiceImpl.APP);
                        Toast.makeText(APP.getApplicationContext(), "Carregando os Tipos de ITERAÇÕES.", Toast.LENGTH_SHORT).show();
                        List<IterationType> iterationTypes = new ArrayList<>();
                        for (IterationTypeDTO iterationTypeDTO : data){
                            iterationTypeDTO.setSyncSatus(SyncSatus.SENT);
                            iterationTypeDTO.getIterationType().setSyncStatus(SyncSatus.SENT);
                            iterationTypes.add(iterationTypeDTO.getIterationType());
                        }
                        evaluationTypeService.saveOrUpdateIterationTypes(data);
                        listener.doOnResponse(BaseRestService.REQUEST_SUCESS, iterationTypes);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(APP.getApplicationContext(), "TIPOS DE ITERAÇÕES CARREGADOS COM SUCESSO", Toast.LENGTH_SHORT).show();
                } else {
                    listener.doOnResponse(REQUEST_NO_DATA, null);
                }
            }

            @Override
            public void onFailure(Call<List<IterationTypeDTO>> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar os Tipos de ITERAÇÕES. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });

    }

}
