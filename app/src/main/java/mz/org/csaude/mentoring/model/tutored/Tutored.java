package mz.org.csaude.mentoring.model.tutored;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.*;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.tutored.TutoredDaoImpl;
import mz.org.csaude.mentoring.dao.user.UserDaoImpl;
import mz.org.csaude.mentoring.dto.tutored.TutoredDTO;
import mz.org.csaude.mentoring.model.career.Career;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.model.user.UserIndividual;

@Data
@NoArgsConstructor
@DatabaseTable(tableName = Tutored.COLUMN_TABLE_NAME, daoClass = TutoredDaoImpl.class)
@EqualsAndHashCode(callSuper=false)
public class Tutored extends BaseModel implements UserIndividual {

    public static final String COLUMN_TABLE_NAME = "tutored";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_CAREER = "career_id";
    public static final String COLUMN_USER = "user_id";

    @DatabaseField(columnName = COLUMN_CODE)
    private String code;

    @DatabaseField(columnName = COLUMN_NAME)
    private String name;

    @DatabaseField(columnName = COLUMN_SURNAME)
    private String surname;

    @DatabaseField(columnName = COLUMN_PHONE_NUMBER)
    private String phoneNumber;

    @DatabaseField(columnName = COLUMN_EMAIL)
    private String email;

    @DatabaseField(columnName = COLUMN_CAREER, canBeNull = false, foreign = true, foreignAutoRefresh = true )
    private Career career;

    @DatabaseField(columnName = COLUMN_USER, canBeNull = true, foreign = true, foreignAutoRefresh = true )
    private User user;

    public Tutored(String code, String name, String surname, String phoneNumber, String email, Career career, User user) {
        this.code = code;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.career = career;
        this.user = user;
    }

    public Tutored(String name, String surname, String phoneNumber, String email, Career career, User user) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.career = career;
        this.user = user;
    }

    public Tutored(TutoredDTO tutoredDTO) {
        this.setUuid(tutoredDTO.getUuid());
        this.setCode(tutoredDTO.getCode());
        this.setEmail(tutoredDTO.getEmail());
        this.setName(tutoredDTO.getName());
        this.setSurname(tutoredDTO.getSurname());
        this.setPhoneNumber(tutoredDTO.getPhoneNumber());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Career getCareer() {
        return career;
    }

    public void setCareer(Career career) {
        this.career = career;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
