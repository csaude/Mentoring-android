package mz.org.csaude.mentoring.workSchedule.rest;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseRestService;
import mz.org.csaude.mentoring.dto.location.HealthFacilityDTO;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.model.location.HealthFacility;
import mz.org.csaude.mentoring.model.location.Location;
import mz.org.csaude.mentoring.service.location.HealthFacilityService;
import mz.org.csaude.mentoring.service.location.HealthFacilityServiceImpl;
import mz.org.csaude.mentoring.service.metadata.LoadMetadataServiceImpl;
import mz.org.csaude.mentoring.util.Utilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthFacilityRestService extends BaseRestService {
    public HealthFacilityRestService(Application application) {
        super(application);
    }
    public void restGetHealthFacility(RestResponseListener<HealthFacility> listener){

        List<String> districts = new ArrayList<>();
        for (Location location : getApplication().getAuthenticatedUser().getEmployee().getLocations()) {
            districts.add(location.getDistrict().getUuid());
        }
       Call<List<HealthFacilityDTO>> callHealthFacilith = syncDataService.getByDistricts(districts);

        callHealthFacilith.enqueue(new Callback<List<HealthFacilityDTO>>() {
            @Override
            public void onResponse(Call<List<HealthFacilityDTO>> call, Response<List<HealthFacilityDTO>> response) {

             List<HealthFacilityDTO> datas = response.body();

             if(datas == null){

             }
                try {

                    List<HealthFacility> healthFacilities = Utilities.parse(datas, HealthFacility.class);
                    HealthFacilityService healthFacilityService = getApplication().getHealthFacilityService();
                    healthFacilityService.savedOrUpdatHealthFacilitys(healthFacilities);

                    listener.doOnResponse(BaseRestService.REQUEST_SUCESS, healthFacilities);
                } catch (SQLException | InvocationTargetException | InstantiationException |
                         NoSuchMethodException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<List<HealthFacilityDTO>> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar as HealthFacility. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });

    }
}
