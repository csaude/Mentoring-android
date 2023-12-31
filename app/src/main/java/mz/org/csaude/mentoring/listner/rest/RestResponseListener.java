package mz.org.csaude.mentoring.listner.rest;

import java.util.HashMap;
import java.util.List;

import mz.org.csaude.mentoring.base.model.BaseModel;


public interface RestResponseListener<T extends BaseModel> {

    default void doOnRestSucessResponse(String flag) {}

    default void doOnRestSucessResponse(T object) {}

    default void doOnRestErrorResponse(String errormsg) {}

    default void doOnRestSucessResponseObjects(String flag, List<T> objects) {}

    default void doOnResponse(String flag, List<T> objects) {

    }

    default void onResponse(String flag, HashMap<String, Object> result) {

    }
}
