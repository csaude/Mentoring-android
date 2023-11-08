package mz.org.csaude.mentoring.model.tutor;

import static mz.org.csaude.mentoring.model.tutor.Tutor.COLUMN_TABLE_NAME;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.csaude.mentoring.adapter.recyclerview.listable.Listble;
import mz.org.csaude.mentoring.base.model.BaseModel;
import mz.org.csaude.mentoring.dao.tutor.TutorDAOImpl;
import mz.org.csaude.mentoring.dao.user.UserDaoImpl;
import mz.org.csaude.mentoring.dto.tutor.TutorDTO;
import mz.org.csaude.mentoring.model.career.Career;
import mz.org.csaude.mentoring.model.partner.Partner;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.model.user.UserIndividual;
import mz.org.csaude.mentoring.util.Utilities;

@DatabaseTable(tableName = COLUMN_TABLE_NAME, daoClass = TutorDAOImpl.class)
public class Tutor extends BaseModel implements UserIndividual {

  public static final String COLUMN_TABLE_NAME = "tutor";
  public static final String COLUMN_EMAIL = "email";
  public static final String COLUMN_CODE = "code";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_SURNAME = "surname";
  public static final String COLUMN_PHONE_NUMBER = "phone_number";
  public static final String COLUMN_CAREER = "career_id";
  public static final String COLUMN_PARTNER= "partner_id";
  public static final String COLUMN_USER= "user_id";

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

  @DatabaseField(columnName = COLUMN_PARTNER, canBeNull = false, foreign = true, foreignAutoRefresh = true )
  private Partner partner;

  @DatabaseField(columnName = COLUMN_USER, canBeNull = false, foreign = true, foreignAutoRefresh = true )
  private User user;

  private List<TutorLocation> tutorLocations;

  public Tutor() {
  }

  public Tutor(TutorDTO tutorDTO) {
    this.setUuid(tutorDTO.getUuid());
    this.code = tutorDTO.getCode();
    this.name = tutorDTO.getName();
    this.surname = tutorDTO.getSunname();
    this.phoneNumber = tutorDTO.getPhoneNumber();
    this.email = tutorDTO.getEmail();
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

  public Partner getPartner() {
    return partner;
  }

  public void setPartner(Partner partner) {
    this.partner = partner;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<TutorLocation> getTutorLocations() {
    return tutorLocations;
  }

  public void setTutorLocations(List<TutorLocation> tutorLocations) {
    this.tutorLocations = tutorLocations;
  }

}
