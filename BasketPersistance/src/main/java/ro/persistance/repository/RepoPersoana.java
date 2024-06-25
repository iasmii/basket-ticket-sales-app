package ro.persistance.repository;


import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.model.Persoana;
import ro.persistance.IRepoPersoana;

public class RepoPersoana implements IRepoPersoana {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger(RepoPersoana.class);

    public RepoPersoana(Properties props){
        logger.info("Initializing RepoPersoana with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    private String cripteaza(String parola){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            byte[] hash = digest.digest(parola.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public Persoana logIn(String username, String parola){
        logger.traceEntry("finding persoana with username {} ",username);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from persoana where username=? and parola=?")){
            preStmt.setString(1,username);
            String criptat=this.cripteaza(parola);
            preStmt.setString(2,criptat);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id=result.getInt("id");
                    String usernameok = result.getString("username");
                    String parolaok = result.getString("parola");
                    Persoana persoana = new Persoana(usernameok,parolaok);
                    persoana.setId(id);
                    logger.traceExit(persoana);
                    return persoana;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No person found with username {}", username);

        return null;
    }

    //@Override
    public Persoana findOne(Integer id) {
        logger.traceEntry("finding persoana with id {} ",id);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from persoana where id=?")){
            preStmt.setInt(1,id);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    String username = result.getString("username");
                    String parola = result.getString("parola");
                    Persoana persoana = new Persoana(username,parola);
                    persoana.setId(id);
                    logger.traceExit(persoana);
                    return persoana;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No person found with id {}", id);

        return null;
    }

    @Override
    public Iterable<Persoana> findAll() {
        logger.traceEntry("finding all persoane ");
        Connection con=dbUtils.getConnection();
        List<Persoana> persoane=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from persoana")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String username = result.getString("username");
                    String parola = result.getString("parola");
                    Persoana persoana = new Persoana(username,parola);
                    persoana.setId(id);
                    persoane.add(persoana);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(persoane);
        return persoane;
    }

    @Override
    public Persoana save(Persoana entity) {
        logger.traceEntry("saving person {} ",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into persoana(username,parola) values (?,?)")){
            preStmt.setString(1,entity.getUsername());
            String criptat=this.cripteaza(entity.getParola());
            preStmt.setString(2,criptat);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
        return entity;
    }

    @Override
    public Persoana delete(Integer integer) {
        return null;
    }

    @Override
    public Persoana update(Persoana Persoana){
        return null;
    }
}