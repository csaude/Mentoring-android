package mz.org.csaude.mentoring.workSchedule.rest;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseRestService;
import mz.org.csaude.mentoring.dto.mentorship.DoorDTO;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.model.mentorship.Door;
import mz.org.csaude.mentoring.service.mentorship.DoorService;
import mz.org.csaude.mentoring.service.mentorship.DoorServiceImpl;
import mz.org.csaude.mentoring.service.metadata.LoadMetadataServiceImpl;
import mz.org.csaude.mentoring.util.SyncSatus;
import mz.org.csaude.mentoring.util.Utilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoorRestService extends BaseRestService {
    public DoorRestService(Application application) {
        super(application);
    }

    public void restGetDoors(RestResponseListener<Door> listener){

        Call<List<DoorDTO>> doorsCall = syncDataService.getDoors();

        doorsCall.enqueue(new Callback<List<DoorDTO>>() {
            @Override
            public void onResponse(Call<List<DoorDTO>> call, Response<List<DoorDTO>> response) {

                List<DoorDTO> data = response.body();

                if(Utilities.listHasElements(data)){
                    try {
                        DoorService doorService = new DoorServiceImpl(LoadMetadataServiceImpl.APP);
                        Toast.makeText(APP.getApplicationContext(), "Carregando as Portas.", Toast.LENGTH_SHORT).show();
                        List<Door> doors = new ArrayList<>();
                        for (DoorDTO doorDTO : data){
                            doorDTO.setSyncSatus(SyncSatus.SENT);
                            doorDTO.getDoor().setSyncStatus(SyncSatus.SENT);
                            doors.add(doorDTO.getDoor());
                        }
                        doorService.saveOrUpdateDoors(data);
                        listener.doOnResponse(BaseRestService.REQUEST_SUCESS, doors);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(APP.getApplicationContext(), "PORTAS CARREGADAS COM SUCESSO", Toast.LENGTH_SHORT).show();
                } else {
                    listener.doOnResponse(REQUEST_NO_DATA, null);
                }
            }

            @Override
            public void onFailure(Call<List<DoorDTO>> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar as Portas. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });

    }

}
