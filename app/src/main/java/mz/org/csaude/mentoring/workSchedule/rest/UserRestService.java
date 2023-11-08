package mz.org.csaude.mentoring.workSchedule.rest;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import mz.org.csaude.mentoring.base.auth.LoginRequest;
import mz.org.csaude.mentoring.base.auth.LoginResponse;
import mz.org.csaude.mentoring.base.auth.SessionManager;
import mz.org.csaude.mentoring.base.service.BaseRestService;
import mz.org.csaude.mentoring.dto.user.UserDTO;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.model.career.Career;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.service.metadata.LoadMetadataServiceImpl;
import mz.org.csaude.mentoring.service.metadata.SyncDataService;
import mz.org.csaude.mentoring.service.user.UserService;
import mz.org.csaude.mentoring.service.user.UserServiceImpl;
import mz.org.csaude.mentoring.service.user.UserSyncService;
import mz.org.csaude.mentoring.util.Utilities;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRestService extends BaseRestService implements UserSyncService {


    private SessionManager sessionManager;

    public UserRestService(Application application, User currentUser) {
        super(application, currentUser);
    }


    public void doOnlineLogin (RestResponseListener listener) {
        this.sessionManager = new SessionManager(application.getApplicationContext());

        SyncDataService syncDataService = getRetrofit().create(SyncDataService.class);

        LoginRequest loginRequest = new LoginRequest(currentUser.getPassword(), currentUser.getUserName());

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), gson.toJson(loginRequest));

        Call<LoginResponse> call = syncDataService.login(body);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    LoginResponse data = response.body();

                    if (Utilities.stringHasValue(data.getAccess_token())) {
                        sessionManager.saveAuthToken(data.getAccess_token());
                        listener.doOnRestSucessResponse(currentUser);
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar metadados. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });
    }

    @Override
    public void getUserByCedencials(RestResponseListener<User> listener) {

        SyncDataService syncDataService = getRetrofit().create(SyncDataService.class);

        Call<UserDTO> call = syncDataService.getByCredencials(currentUser.getUserName(), currentUser.getPassword());

        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.code() == 200) {
                    UserDTO data = response.body();

                    try {

                        User user1 = new User(data);

                        UserService userService = new UserServiceImpl(LoadMetadataServiceImpl.APP);
                        userService.savedOrUpdateUser(user1);
                        listener.doOnResponse(BaseRestService.REQUEST_SUCESS, Collections.singletonList(user1));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar metadados. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });
    }
}
