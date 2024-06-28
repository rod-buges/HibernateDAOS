package DAOs;

import Hibernates.Funcionario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FuncionarioDAO {
    Configuration cfg = new Configuration().configure("/hibernate.cfg.xml");
    SessionFactory factory = cfg.buildSessionFactory();
    List<Funcionario> funcionarios = new ArrayList<>();
    Funcionario funcionario;

    // efetuar nossa consulta, ou inserção na nossa base de dados, confirmando que a tarefa foi concluída de forma correta
    static void inSession(EntityManagerFactory factory, Consumer<EntityManager> work) {
        var entityManager = factory.createEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            work.accept(entityManager);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
        finally {
            entityManager.close();
        }
    }

    public List<Funcionario> findAll() {
        inSession(factory, entityManager -> {
            funcionarios = entityManager.createQuery("select d from Funcionario d", Funcionario.class).getResultList();
        });
        return funcionarios;
    }

    public void save(Funcionario funcionario) {
        inSession(factory, entityManager -> {
            entityManager.persist(funcionario);
        });
    }

    public void delete(Funcionario funcionario) {
        inSession(factory, entityManager -> {
            entityManager.remove(funcionario);
        });
    }

    public Funcionario findById(int id) {
        inSession(factory, entityManager -> {
            funcionario = entityManager.find(Funcionario.class, id);
        });
        return funcionario;
    }

    public void update(Funcionario funcionario) {
        inSession(factory, entityManager -> {
            entityManager.merge(funcionario);
        });
    }
}
