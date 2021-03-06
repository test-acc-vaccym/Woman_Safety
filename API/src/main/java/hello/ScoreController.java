package hello;
import hello.Entity.SurveysEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import hello.Entity.UserScoreEntity;

@RestController
public class ScoreController {
    private static final SessionFactory ourSessionFactory;
    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    @RequestMapping(value="/api/tests/score", params={"phone", "score", "survey"})
    public void  setScore(@RequestParam("phone") String phone, @RequestParam("score") int score, @RequestParam("survey") int id ){

        final Session session = getSession();
        try {
        session.beginTransaction();
        SurveysEntity surveysEntity = (SurveysEntity)session.get(SurveysEntity.class, id);
            System.out.println(surveysEntity.getIdSurvey());
        UserScoreEntity userScoreEntity =new UserScoreEntity(phone, score);
        userScoreEntity.setSurveyBySurvey(surveysEntity);
        session.save(userScoreEntity);
        session.getTransaction().commit();
        } finally {
            session.close();
        }


    }

}
