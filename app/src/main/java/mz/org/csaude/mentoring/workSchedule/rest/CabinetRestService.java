package mz.org.csaude.mentoring.workSchedule.rest;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import mz.org.csaude.mentoring.service.location.CabinetService;
import mz.org.csaude.mentoring.service.location.CabinetServiceImpl;
import mz.org.csaude.mentoring.service.metadata.LoadMetadataServiceImpl;
import mz.org.csaude.mentoring.util.Utilities;
import retrofit2.Call;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseRestService;
import mz.org.csaude.mentoring.dto.location.CabinetDTO;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.model.location.Cabinet;
import mz.org.csaude.mentoring.model.user.User;
import retrofit2.Callback;
import retrofit2.Response;

public class CabinetRestService extends BaseRestService {
    public CabinetRestService(Application application) {
        super(application);
    }

    public void restGetCabinets(long offSet, long limit, RestResponseListener<Cabinet> listener){

        Call<List<CabinetDTO>> cabinetsCall = syncDataService.getCabinets(offSet, limit);

        cabinetsCall.enqueue(new Callback<List<CabinetDTO>>() {
            @Override
            public void onResponse(Call<List<CabinetDTO>> call, Response<List<CabinetDTO>> response) {

                List<CabinetDTO> data = response.body();

                if(!Utilities.listHasElements(data)){
                    listener.doOnResponse(REQUEST_NO_DATA, null);
                } else {
                    try {

                        CabinetService cabinetService = new CabinetServiceImpl(LoadMetadataServiceImpl.APP);
                        Toast.makeText(APP.getApplicationContext(), "Carregando os Cabinet", Toast.LENGTH_SHORT).show();
                        cabinetService.saveOrUpdateCabinets(data);
                        List<Cabinet> cabinets = new ArrayList<>();

                        for (CabinetDTO cabinetDTO : data){
                            cabinets.add(new Cabinet(cabinetDTO));
                        }
                        listener.doOnResponse(BaseRestService.REQUEST_SUCESS, cabinets);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(APP.getApplicationContext(), "CABINETS CARREGADAS COM SUCESSO", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<CabinetDTO>> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar os Cabinets. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });

    }

}
