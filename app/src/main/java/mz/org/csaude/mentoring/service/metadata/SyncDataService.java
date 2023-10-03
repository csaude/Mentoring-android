package mz.org.csaude.mentoring.service.metadata;

import org.json.JSONObject;

import mz.org.csaude.mentoring.base.auth.LoginRequest;
import mz.org.csaude.mentoring.base.auth.LoginResponse;
import mz.org.csaude.mentoring.dto.career.CareerDTO;
import mz.org.csaude.mentoring.dto.career.CareerTypeDTO;
import mz.org.csaude.mentoring.dto.location.CabinetDTO;
import mz.org.csaude.mentoring.dto.location.HealthFacilityDTO;
import mz.org.csaude.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.csaude.mentoring.dto.setting.SettingDTO;
import mz.org.csaude.mentoring.dto.tutor.FormDTO;
import mz.org.csaude.mentoring.dto.tutor.TutorDTO;
import mz.org.csaude.mentoring.dto.tutored.TutoredDTO;
import mz.org.csaude.mentoring.dto.user.UserDTO;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

/**
 * Created by ialuj
 */
public interface SyncDataService {

    @GET("/settings/tutor/{uuid}/{offset}/{limit}")
    Call<List<SettingDTO>> getSettings(@Path("uuid") final String uuid, @Path("offset") long offset, @Path("limit") long limit);

    @GET("/healthFacilities/tutor/{uuid}")
    Call<List<HealthFacilityDTO>> getHealthFacilities(@Path("uuid") final String uuid);

    @GET("/cabinets/{offset}/{limit}")
    Call<List<CabinetDTO>> getCabinets(@Path("offset") long offset, @Path("limit") long limit);

    @GET("/careers/career-types")
    Call<List<CareerTypeDTO>> getCareerTypes();

    @GET("/careers/{offset}/{limit}")
    Call<List<CareerDTO>> getCareers(@Path("offset") long offset, @Path("limit") long limit);

    @GET("/tutored/{offset}/{limit}")
    Call<List<TutoredDTO>> getTutoreds(@Path("offset") long offset, @Path("limit") long limit);

    @POST("/login")
    Call<LoginResponse> login(@Body RequestBody body);

    @GET("/user/getByCredencials/{username}/{password}")
    Call<UserDTO> getByCredencials(@Path("username") final String username, @Path("password") final String password);

    @GET("/tutor/tutors/{limit}/{offset}")
    Call<List<TutorDTO>> getTutors(@Path("limit") long limit , @Path("offset") long offset);

    @GET("/tutor/user/{userUuid}")
    Call<TutorDTO> getTutorByUserUuid(@Path("userUuid") String userUuid);

    @GET("/forms/programaticarea/{programaticaAreaUuid}")
    Call<List<FormDTO>> getFormByProgrammaticAreaUuid(@Path("programaticaAreaUuid") String programaticaAreaUuid);
    @GET("/forms/form/{limit}/{offset}")
    Call<List<FormDTO>> getForms(@Path("limit") long limit, @Path("offset") long offset);

    @GET("/programmaticareas/tutor-progammatic-area/{tutorUuid}")
    Call<List<ProgrammaticAreaDTO>> getProgrammaticAreaByTutorProgrammaticAreaUuid(@Path("tutorUuid") String tutorUuid);

    @GET("/programmaticareas/programmaticarea/")
    Call<List<ProgrammaticAreaDTO>> getProgrammatics(@Path("limit") long limit, @Path("offset") long offset);

}
