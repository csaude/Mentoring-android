package mz.org.csaude.mentoring.workSchedule.rest;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseRestService;
import mz.org.csaude.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.model.programmaticArea.ProgrammaticArea;
import mz.org.csaude.mentoring.service.ProgrammaticArea.ProgrammaticAreaService;
import mz.org.csaude.mentoring.service.ProgrammaticArea.ProgrammaticAreaServiceImpl;
import mz.org.csaude.mentoring.service.metadata.LoadMetadataServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgrammaticAreaRestService extends BaseRestService {

    public ProgrammaticAreaRestService(Application application) {
        super(application);
    }

    public  void restGetProgrammings(long limit, long offset, RestResponseListener<ProgrammaticArea> listener){
        Call<List<ProgrammaticAreaDTO>> programmatiCall = syncDataService.getProgrammatics(limit, offset);

        programmatiCall.enqueue(new Callback<List<ProgrammaticAreaDTO>>() {
            @Override
            public void onResponse(Call<List<ProgrammaticAreaDTO>> call, Response<List<ProgrammaticAreaDTO>> response) {

              List<ProgrammaticAreaDTO> data = response.body();

                try {

                    ProgrammaticAreaService programmaticAreaService = new ProgrammaticAreaServiceImpl(LoadMetadataServiceImpl.APP);
                    programmaticAreaService.savedOrUpdateProgrammaticAreas(data);
                    List<ProgrammaticArea> programmaticAreas = new ArrayList<>();

                    for (ProgrammaticAreaDTO programmaticAreaDTO : data){
                        programmaticAreas.add(new ProgrammaticArea(programmaticAreaDTO));
                    }
                    listener.doOnResponse(BaseRestService.REQUEST_SUCESS, programmaticAreas);
                    Toast.makeText(APP.getApplicationContext(), "Carregando as PROGRAMMATICAREA...", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(Call<List<ProgrammaticAreaDTO>> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar os TIPOS DE PROGRAMMATICAREA. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });
    }
}
