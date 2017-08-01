package ru.hd.olaf.util;

import javax.persistence.Transient;

/**
 * Created by Olaf on 31.07.2017.
 *
 * Служебный класс, реализующий возвращаемый в формате JSON результат ручной работы с БД (поиск, создание, удаление)
 */
public class JsonResponse {
    private JsonResponseType jsonResponseType;      //Тип результата
    private String message;                         //Описание результата
    private EntityData entityData;                  //Объект, содержащий в себе данные найденной пары accounts-clients

    public JsonResponse() {
    }

    public JsonResponse(JsonResponseType jsonResponseType, String message) {
        this.jsonResponseType = jsonResponseType;
        this.message = message;
    }

    public JsonResponse(JsonResponseType jsonResponseType, EntityData entityData) {
        this.jsonResponseType = jsonResponseType;
        this.entityData = entityData;
    }

    public JsonResponse(JsonResponseType jsonResponseType, String message, EntityData entityData) {
        this.jsonResponseType = jsonResponseType;
        this.message = message;
        this.entityData = entityData;
    }

    public JsonResponseType getJsonResponseType() {
        return jsonResponseType;
    }

    public void setJsonResponseType(JsonResponseType jsonResponseType) {
        this.jsonResponseType = jsonResponseType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EntityData getEntityData() {
        return entityData;
    }

    public void setEntityData(EntityData entityData) {
        this.entityData = entityData;
    }

    @Override
    public String toString() {
        return "JsonResponse{" +
                "jsonResponseType=" + jsonResponseType +
                ", message='" + (message !=null ? message : "")+ '\'' +
                ", entityData=" + (entityData != null ? entityData : "") +
                '}';
    }
}
