package learn.solarFarm.data;

import learn.solarFarm.models.Material;
import learn.solarFarm.models.Panel;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class PanelJdbcTemplateRepository implements PanelRepository{

    private final JdbcTemplate jdbcTemplate;

    public PanelJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Panel> mapper = (resultSet, rowNum) -> {
        Panel panel = new Panel();
        panel.setPanelId(resultSet.getInt("panel_id"));
        panel.setSection(resultSet.getString("section"));
        panel.setRow(resultSet.getInt("row"));
        panel.setCol(resultSet.getInt("column"));
        panel.setYear(resultSet.getInt("year"));
        String materialType = resultSet.getString("material");
        panel.setMaterial(Material.valueOf(materialType));
        panel.setTracking(resultSet.getBoolean("isTracking"));
        return panel;
    };


    @Override
    public List<Panel> findAll() throws DataAccessException {
        final String sql = "select * from panel;";
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public List<Panel> findBySection(String section) throws DataAccessException {
        final String sql = "select * from panel where section = ?;";
        try {
            return jdbcTemplate.query(sql, mapper, section);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Panel findById(int panelId) throws DataAccessException {
        final String sql = "select * where panel_id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, mapper, panelId);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Panel add(Panel panel) throws DataAccessException {
        final String sql = "insert into panel (`section`, `row`, `column`, `year`, `material`, `isTracking`) values (?,?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, panel.getSection());
            ps.setInt(2, panel.getRow());
            ps.setInt(3, panel.getCol());
            ps.setInt(4, panel.getYear());
            ps.setString(5, panel.getMaterial().toString());
            ps.setBoolean(6, panel.isTracking());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        panel.setPanelId(keyHolder.getKey().intValue());
        return panel;
    }

    @Override
    public boolean update(Panel panel) throws DataAccessException {
        final String sql = "update panel set "
                + "`section` = ?, "
                + "`row` = ? "
                + "`column` = ? "
                + "`year` = ? "
                + "`material` = ? "
                + "`isTracking` = ? "
                + "where panel_id = ?;";

        int rowsUpdated = jdbcTemplate.update(sql,
                panel.getSection(), panel.getRow(), panel.getCol(), panel.getYear(),
                panel.getMaterial().toString(), panel.isTracking(), panel.getPanelId());

        return rowsUpdated > 0;
    }

    @Override
    public boolean deleteById(int panelId) throws DataAccessException {
        final String sql = "delete from panel where panel_id = ?;";

        int rowsUpdated = jdbcTemplate.update(sql, panelId);

        return rowsUpdated > 0;
    }
}
