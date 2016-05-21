package com.lehanh.pama.db.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.lehanh.pama.catagory.Catagory;
import com.lehanh.pama.catagory.CatagoryType;
import com.lehanh.pama.catagory.DrugCatagory;
import com.lehanh.pama.catagory.IContainJsonDataCatagory;
import com.lehanh.pama.db.DatabaseManager;
import com.lehanh.pama.ui.util.MainApplication;
import com.lehanh.pama.util.PamaHome;

public class CatagoryDao implements IDao {

	private static final String CAT_TABLE = "catagory";
	private static final String ID_COL = "id";
	private static final String OLD_ID_COL = "old_id";
	private static final String NAME_COL = "name";
	private static final String SYMBOL_COL = "symbol";
	private static final String TYPE_COL = "cat_type";
	private static final String DESC_COL = "cat_desc";
	private static final String REFIDS_COL = "ref_ids";
	private static final String OTHERDATA_COL = "other_data";
	
	public List<Catagory> loadAllCatagory() throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DatabaseManager.getInstance().getConn();
			ps = conn.prepareStatement("select * from catagory");
			rs = ps.executeQuery();
			
			List<Catagory> result = new LinkedList<Catagory>();
			Catagory caC;
			while (rs.next()) {
				caC = populate(rs);
				result.add(caC);
			}
			return result;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	private Catagory populate(ResultSet rs) throws SQLException {
		Catagory cat = CatagoryType.valueOf(rs.getString(TYPE_COL)).createCatalog(rs.getLong(ID_COL));
		cat.setName(rs.getString(NAME_COL));
		cat.setDesc(rs.getString(DESC_COL));
		cat.setSymbol(rs.getString(SYMBOL_COL));
		cat.setRefIdsText(rs.getString(REFIDS_COL));
		cat.setOtherDataText(rs.getString(OTHERDATA_COL));
		cat.setOldId(rs.getLong(OLD_ID_COL));
		return cat;
	}

	public Long insert(Catagory item) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DatabaseManager.getInstance().getConn();
			ps = conn.prepareStatement(
					"insert into " + CAT_TABLE + 
						" (old_id, name, symbol, cat_type, cat_desc, ref_ids, other_data) " + 
						" values (?, ?, ?, ?, ?, ?, ? ) ", Statement.RETURN_GENERATED_KEYS);
			int i = 1;
			ps.setLong(i++, item.getOldId());
			ps.setString(i++, item.getName());
			ps.setString(i++, item.getSymbol());
			ps.setString(i++, item.getType().toString());
			ps.setString(i++, item.getDesc());
			ps.setString(i++, item.getRefIdsText());
			if (item instanceof IContainJsonDataCatagory) {
				item.setOtherDataText(((IContainJsonDataCatagory) item).toOtherDataAsText());
			}
			ps.setString(i++, item.getOtherDataText());
			System.out.println("SQL: " + ps);
			int resultUpdate = ps.executeUpdate();
			if (resultUpdate <= 0) {
				return 0l;
			}
			ResultSet resultIdKey = ps.getGeneratedKeys();
			if (resultIdKey.next()) {
				Long resultId = resultIdKey.getLong(1);
				item.setId(resultId);
				return resultId;
			}
			throw new SQLException("Insert statement did not generate an AutoID"); //$NON-NLS-1$
			
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public void update(Catagory item) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DatabaseManager.getInstance().getConn();
			ps = conn.prepareStatement(
					"update " + CAT_TABLE + 
					" set old_id=?, name=?, symbol=?, cat_type=?, cat_desc=?, ref_ids=?, other_data=? " +
					" where id=? ");

			int i = 1;
			ps.setLong(i++, item.getOldId());
			ps.setString(i++, item.getName());
			ps.setString(i++, item.getSymbol());
			ps.setString(i++, item.getType().toString());
			ps.setString(i++, item.getDesc());
			ps.setString(i++, item.getRefIdsText());

			if (item instanceof IContainJsonDataCatagory) {
				item.setOtherDataText(((IContainJsonDataCatagory) item).toOtherDataAsText());
			}
			ps.setString(i++, item.getOtherDataText());

			ps.setLong(i++, item.getId());
			System.out.println("SQL: " + ps);
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	public void deleteAllType(CatagoryType catType) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DatabaseManager.getInstance().getConn();
			ps = conn.prepareStatement("delete FROM " + CAT_TABLE + " where " + TYPE_COL + " = '"+ catType.name() + "'");
			System.out.println("SQL: " + ps);
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}	
	}
	
	public void internalDeleteAll() throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DatabaseManager.getInstance().getConn();
			ps = conn.prepareStatement("delete FROM " + CAT_TABLE);
			System.out.println("SQL: " + ps);
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}		
	}
	
	public static void main(String[] args) throws SQLException, IOException {
		PamaHome.application = new MainApplication();
		DatabaseManager.initialize();
		CatagoryDao catDao = new CatagoryDao();
		catDao.internalDeleteAll();
	}
	
	public static void main2(String[] args) {
		CatagoryDao catDao = new CatagoryDao();
		try {
			PamaHome.application = new MainApplication();
			DatabaseManager.initialize();
			catDao.internalDeleteAll();
			
			// sample service
			long sg1 = catDao.insert(new Catagory(CatagoryType.SURGERY, "Nang mui silat", "Nose surgery silat", "Nang mui silat"));
			long sg2 = catDao.insert(new Catagory(CatagoryType.SURGERY, "Nang mui cau truc", "Nose surgery modern", "Nang mui cau truc"));
			long sg3 = catDao.insert(new Catagory(CatagoryType.SURGERY, "Chỉnh mũi gồ", "Nose surgery go", "Chỉnh mũi gồ"));
			
			long d1 = catDao.insert(new Catagory(CatagoryType.DIAGNOSE, "Mũi thap tai nan", "Mũi thap tai nan"/*, sg1 + "|" + sg2*/));
			long d2 = catDao.insert(new Catagory(CatagoryType.DIAGNOSE, "Mũi thap bam sinh", "Mũi thap bam sinh"/*, sg1 + "|" + sg2*/));
			long d3 = catDao.insert(new Catagory(CatagoryType.DIAGNOSE, "Mũi gồ", "Mũi gồ"/*, String.valueOf(sg3)*/));
			
			long p1 = catDao.insert(new Catagory(CatagoryType.PROGNOSTIC, "Mũi Thap le te", "Mũi Thap le te"/*, d2 + ""*/));
			long p2 = catDao.insert(new Catagory(CatagoryType.PROGNOSTIC, "Mũi gãy", "Mũi gãy"/*, d1 + ""*/));
			long p3 = catDao.insert(new Catagory(CatagoryType.PROGNOSTIC, "Mũi dập", "Mũi dập"/*, d1 + ""*/));
			long p4 = catDao.insert(new Catagory(CatagoryType.PROGNOSTIC, "Mũi gồ", "Mũi gồ"/*, d3 + ""*/));
			
			long s1 = catDao.insert(new Catagory(CatagoryType.SERVICE, "Nâng mũi", "Nâng mũi"/*, p1 + "|" + p2 + "|" + p3 + "|" + p4*/)
												.addRef(CatagoryType.PROGNOSTIC, p1, p2, p3, p4)
												.addRef(CatagoryType.DIAGNOSE, d1, d2, d3)
												.addRef(CatagoryType.SURGERY, sg1, sg2, sg3)
									);
			catDao.insert(new Catagory(CatagoryType.SERVICE, "Nâng ngực", "Nâng ngực"));

			// initial DR
			catDao.insert(new Catagory(CatagoryType.DR, "LE Hanh", "LE Hanh"));
			
			// Insert data for Drugs
//			1	Cravit 500mg	1	viên	1	1	viên	chiều	uống	Uống sau khi ăn	1	MI1248
//			2	Vita C 500mg	1	viên	1	1	viên	trưa	uống	Uống sau khi ăn	1	MI83
//			3	Omega 3	2	viên	2	1	viên	sáng,chiều	uống	Uống trong khi ăn	1	MI251
//			5	Rotunda	1	viên	1	1	viên	tối	uống	uống trước khi đi ngủ 30 phút	1	MI165
//			6	Povidine Solution	1	chai	2	1	ít	sáng,tối	bôi	Bôi lên vết mổ	1	MI207
//			7	Laenec PO	2	viên	2	1	Viên	sáng,tối	uống	Uống sau khi ăn	0	
//			4	Efferalgan codein	3	viên	3	1	viên	sáng,trưa,chiều	uống	Hòa tan trong nước	1	MI67
//			8	Rhinex	1	chai	2	1	ít	sáng,chiều	nhỏ mũi	nhỏ mũi khi nghẹt	1	MI257
			
//			name, desc, use, total, unit, numDay, perDay, numSs, perSs, ss, note
			catDao.insert(new DrugCatagory("Cravit 500mg", "", "uống", 1, "viên", "viên", 1, 1, 1, 1, "chiều", "Uống sau khi ăn"));
			catDao.insert(new DrugCatagory("Vita C 500mg", "", "uống", 1, "viên", "viên", 1, 1, 1, 1, "trưa", "Uống sau khi ăn"));
			catDao.insert(new DrugCatagory("Omega 3", "", "uống", 1, "viên", "viên", 1, 1, 1, 2, "sáng,chiều", "Uống trong khi ăn"));
			catDao.insert(new DrugCatagory("Rotunda", "", "uống", 1, "viên", "viên", 1, 1, 1, 1, "tối", "Uống sau khi ăn"));
			catDao.insert(new DrugCatagory("Povidine Solution", "", "uống", 1, "chai", "ít", 1, 1, 1, 2, "sáng,tối", "Uống sau khi ăn"));
			catDao.insert(new DrugCatagory("Laenec PO", "", "uống", 1, "viên", "viên", 1, 1, 1, 2, "sáng,tối", "Uống sau khi ăn"));
			catDao.insert(new DrugCatagory("Efferalgan codein", "", "uống", 1, "viên", "viên", 1, 1, 1, 3, "sáng,trưa,chiều", "Uống sau khi ăn"));
			catDao.insert(new DrugCatagory("Rhinex", "", "nhỏ mũi", 1, "chai", "ít", 1, 1, 1, 2, "sáng,chiều", "nhỏ mũi khi nghẹt"));
			
			List<Catagory> result = catDao.loadAllCatagory();
			System.out.println("result " + result.size());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}