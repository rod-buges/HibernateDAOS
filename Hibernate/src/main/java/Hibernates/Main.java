package Hibernates;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //Carrega as configurações do arquivo hibernat.cfg.xml
        Configuration cfg = new Configuration().configure("/hibernate.cfg.xml");
        //Cria uma factory
        SessionFactory factory = cfg.buildSessionFactory();
        //Cria uma nova session
        Session session = factory.openSession();
        //Inicia uma nova transaction
        session.beginTransaction();

        //Cria um novo departamento
        Departamento departamento = new Departamento();
        departamento.setNome("Suporte");
        //grava o novo departamento no banco de dados
        session.persist(departamento);

        //Criando um departamento utilizando o Builder
        departamento = Departamento.builder().nome("Logística").build();
        //grava o novo departamento no banco de dados
        session.persist(departamento);

        //Buscando um Departamento do banco de dados com id=1
        departamento = session.get(Departamento.class,1);

        //Criando uma query, com parâmetros
        departamento = session.
                createQuery("SELECT d FROM Departamento d WHERE d.nome=:nomeDepartamento", Departamento.class). //buscando um departamento no banco de dados
                        setParameter("nomeDepartamento", "Marketing"). //definindo o valor do parametro nomeDepartamento cujo é Logística
                        getSingleResultOrNull();
        if(departamento != null) {
            System.out.println("Departamento encontrado: " + departamento.getNome());
        } else {
            System.out.println("Departamento não encontrado.");
        }

        //Criando um novo Funcionário
        Funcionario funcionario = Funcionario.
                builder().
                nome("Angelo").
                salario(3000.0).
                departamento(departamento).
                build();

        //Grava o novo Funcionário e seu respectivo Departamento
        session.persist(funcionario);

        session.getTransaction().commit();
        //Encerrando conexão
        session.close();
        factory.close();
}