package ro.persistance.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ro.model.Meci;
import ro.persistance.IRepoMeci;

@Component
public class RepoMeci implements IRepoMeci {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger(RepoMeci.class);

    public RepoMeci(Properties props){
        logger.info("Initializing RepoMeci with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Meci findOne(Integer id) {
        logger.traceEntry("finding match with id {} ",id);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from meci where id=?")){
            preStmt.setInt(1,id);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    String nume = result.getString("nume");
                    int nrLocuri = result.getInt("locuri");
                    float pret=result.getFloat("pret");
                    Meci meci = new Meci(nume, nrLocuri, pret);
                    meci.setId(id);
                    logger.traceExit(meci);
                    return meci;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No match found with id {}", id);

        return null;
    }

    @Override
    public Iterable<Meci> findAll() {
        logger.traceEntry("finding matches");
        Connection con=dbUtils.getConnection();
        List<Meci> meciuri=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select m.id,m.nume,m.locuri - ifnull(b.locuriVandute, 0) as locuriRamase,m.pret from meci m left join (select meci,sum(locuri) as locuriVandute from bilet b group by meci) b on (m.id = b.meci)")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    //System.out.println("Retrieved ID from database: " + id);
                    String nume = result.getString("nume");
                    int nrLocuri = result.getInt("locuriRamase");
                    float pret=result.getFloat("pret");
                    Meci meci = new Meci(nume, nrLocuri, pret);
                    meci.setId(id);
                    meciuri.add(meci);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(meciuri);
        return meciuri;
    }

    public Iterable<Meci> findDisponibile() {
        logger.traceEntry("finding matches with seats");
        Connection con=dbUtils.getConnection();
        List<Meci> meciuri=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select m.id,m.nume,m.locuri - ifnull(b.locuriVandute, 0) as locuriRamase,m.pret from meci m left join (select meci,sum(locuri) as locuriVandute from bilet b group by meci) b on (m.id = b.meci and m.locuri>b.locuriVandute) WHERE m.locuri - ifnull(b.locuriVandute, 0) > 0 order by m.locuri-b.locuriVandute desc;")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    int nrLocuri = result.getInt("locuriRamase");
                    float pret=result.getFloat("pret");
                    Meci meci = new Meci(nume, nrLocuri, pret);
                    meci.setId(id);
                    meciuri.add(meci);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(meciuri);
        return meciuri;
    }


    @Override
    public Meci save(Meci entity) {
        logger.traceEntry("saving match {} ",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into meci(nume,pret,locuri) values (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1,entity.getNume());
            preStmt.setFloat(2,entity.getPret());
            preStmt.setInt(3,entity.getNrLocuri());
            int result=preStmt.executeUpdate();

            // Retrieve the generated keys
            try (ResultSet generatedKeys = preStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                    System.out.println("generated key "+generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating match failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
        System.out.println("id save "+entity.getId());
        return entity;
    }

    /*
    @Override
    public Meci save(Meci entity) {
        logger.traceEntry("saving match {} ",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into meci(nume,pret,locuri) values (?,?,?)")){
            preStmt.setString(1,entity.getNume());
            preStmt.setFloat(2,entity.getPret());
            preStmt.setInt(3,entity.getNrLocuri());
            int result=preStmt.executeUpdate();
            try (ResultSet generatedKeys = preStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(0));
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
        return entity;
    }*/

    @Override
    public Meci delete(Integer id) {
        logger.traceEntry("deleting match with id {}", id);
        Connection con = dbUtils.getConnection();
        Meci meci = findOne(id);
        if (meci != null) {
            try (PreparedStatement preStmt = con.prepareStatement("delete from meci where id=?")) {
                preStmt.setInt(1, id);
                int result = preStmt.executeUpdate();
            } catch (SQLException ex) {
                logger.error(ex);
                System.out.println("Error DB " + ex);
            }
        }
        logger.traceExit();
        return meci;
    }

    @Override
    public Meci update(Meci meci) {
        logger.traceEntry("updating match {}", meci);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update meci set nume=?, pret=?, locuri=? where id=?")) {
            preStmt.setString(1, meci.getNume());
            preStmt.setFloat(2, meci.getPret());
            preStmt.setInt(3, meci.getNrLocuri());
            preStmt.setInt(4, meci.getId());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
        return meci;
    }
}