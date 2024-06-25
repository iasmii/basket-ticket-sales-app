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
import ro.model.Bilet;
import ro.persistance.IRepoBilet;

public class RepoBilet implements IRepoBilet {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger(RepoBilet.class);

    public RepoBilet(Properties props) {
        logger.info("Initializing RepoBilet with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Bilet findOne(Integer id) {
        logger.traceEntry("finding ticket with id {} ", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from bilet where id=?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String client = result.getString("client");
                    int nrLocuri = result.getInt("locuri");
                    int meci = result.getInt("meci");
                    Bilet bilet = new Bilet(meci, client, nrLocuri);
                    bilet.setId(id);
                    logger.traceExit(bilet);
                    return bilet;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No ticket found with id {}", id);

        return null;
    }

    @Override
    public Iterable<Bilet> findAll() {
        logger.traceEntry("finding all tickets");
        Connection con = dbUtils.getConnection();
        List<Bilet> bilete = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from bilet")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String client = result.getString("client");
                    int nrLocuri = result.getInt("locuri");
                    int meci = result.getInt("meci");
                    Bilet bilet = new Bilet(meci, client, nrLocuri);
                    bilet.setId(id);
                    bilete.add(bilet);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(bilete);
        return bilete;
    }

    @Override
    public Bilet save(Bilet entity) {
        logger.traceEntry("saving ticket {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into bilet(meci,client,locuri) values (?,?,?)")) {
            preStmt.setInt(1, entity.getMeci());
            preStmt.setString(2, entity.getNumeClient());
            preStmt.setInt(3, entity.getNrLocuri());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
        return entity;
    }

    @Override
    public Bilet delete(Integer integer) {
        return null;
    }

    @Override
    public Bilet update(Bilet Bilet) {
        return null;
    }
}