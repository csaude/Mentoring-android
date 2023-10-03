package mz.org.csaude.mentoring.workSchedule.rest;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseRestService;
import mz.org.csaude.mentoring.dto.tutor.FormDTO;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.model.form.Form;
import mz.org.csaude.mentoring.service.form.FormService;
import mz.org.csaude.mentoring.service.form.FormServiceImpl;
import mz.org.csaude.mentoring.service.metadata.LoadMetadataServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormRestService extends BaseRestService {

    public FormRestService(Application application) {
        super(application);
    }

    public static void resGetForms(long limit, long offset, RestResponseListener<Form> listener){

        Call<List<FormDTO>> formCalls = syncDataService.getForms(limit, offset);

        formCalls.enqueue(new Callback<List<FormDTO>>() {
            @Override
            public void onResponse(Call<List<FormDTO>> call, Response<List<FormDTO>> response) {
              List<FormDTO> data =  response.body();

              if(data == null) {
                  // do it
              }
                  try {
                  FormService formService = new FormServiceImpl(LoadMetadataServiceImpl.APP);
                  Toast.makeText(APP.getApplicationContext(), "Carregando as FORMS...", Toast.LENGTH_SHORT).show();
                  List<Form> forms = new ArrayList<>();
                  formService.savedOrUpdateForms(data);

                      for(FormDTO formDTO :data){
                          forms.add(new Form(formDTO));
                      }
                      listener.doOnResponse(BaseRestService.REQUEST_SUCESS, forms);
                  } catch (SQLException e) {
                      throw new RuntimeException(e);
                  }
                  Toast.makeText(APP.getApplicationContext(), "FORMS carregadas com sucesso!", Toast.LENGTH_SHORT).show();
              }

            @Override
            public void onFailure(Call<List<FormDTO>> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar os TIPOS DE FORMS. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);

            }
        });

    }
}