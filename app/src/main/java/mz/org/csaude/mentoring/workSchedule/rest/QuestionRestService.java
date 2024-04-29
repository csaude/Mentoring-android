package mz.org.csaude.mentoring.workSchedule.rest;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseRestService;
import mz.org.csaude.mentoring.dto.question.QuestionDTO;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.model.question.Question;
import mz.org.csaude.mentoring.service.metadata.LoadMetadataServiceImpl;
import mz.org.csaude.mentoring.service.question.QuestionService;
import mz.org.csaude.mentoring.service.question.QuestionServiceImpl;
import mz.org.csaude.mentoring.util.SyncSatus;
import mz.org.csaude.mentoring.util.Utilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionRestService extends BaseRestService {
    public QuestionRestService(Application application) {
        super(application);
    }

    public void restGetQuestions(RestResponseListener<Question> listener){

        Call<List<QuestionDTO>> questionsCall = syncDataService.getAllQuestions();

        questionsCall.enqueue(new Callback<List<QuestionDTO>>() {
            @Override
            public void onResponse(Call<List<QuestionDTO>> call, Response<List<QuestionDTO>> response) {

                List<QuestionDTO> data = response.body();

                if(Utilities.listHasElements(data)){
                    try {
                        QuestionService questionService = new QuestionServiceImpl(LoadMetadataServiceImpl.APP);
                        Toast.makeText(APP.getApplicationContext(), "Carregando as Questões.", Toast.LENGTH_SHORT).show();
                        List<Question> questions = new ArrayList<>();
                        for (QuestionDTO questionDTO : data){
                            questionDTO.setSyncSatus(SyncSatus.SENT);
                            questionDTO.getQuestionObj().setSyncStatus(SyncSatus.SENT);
                            questions.add(questionDTO.getQuestionObj());
                        }
                        questionService.saveOrUpdateQuestions(data);
                        listener.doOnResponse(BaseRestService.REQUEST_SUCESS, questions);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(APP.getApplicationContext(), "QUESTÕES CARREGADAS COM SUCESSO", Toast.LENGTH_SHORT).show();
                } else {
                    listener.doOnResponse(REQUEST_NO_DATA, null);
                }
            }

            @Override
            public void onFailure(Call<List<QuestionDTO>> call, Throwable t) {
                Toast.makeText(APP.getApplicationContext(), "Não foi possivel carregar as Questões. Tente mais tarde....", Toast.LENGTH_SHORT).show();
                Log.i("METADATA LOAD --", t.getMessage(), t);
            }
        });

    }

}
