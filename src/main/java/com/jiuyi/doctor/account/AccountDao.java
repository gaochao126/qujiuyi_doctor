package com.jiuyi.doctor.account;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.account.model.AccountDetail;
import com.jiuyi.doctor.account.model.AccountInfo;
import com.jiuyi.doctor.account.model.Withdraw;
import com.jiuyi.doctor.account.model.Withdraw.WithdrawStatus;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 下午3:45:20
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class AccountDao extends DbBase {

	private static final String SELECT_PASSWORD = "SELECT `password` FROM `t_doctor_account` WHERE `doctorId`=?";
	private static final String SELECT_ACCOUNT = "SELECT * FROM `t_doctor_account` WHERE `doctorId`=?";
	private static final String SELECT_BALANCE = "SELECT `balance` FROM `t_doctor_account` WHERE `doctorId`=?";
	private static final String SELECT_TOTAL_INCOME = "SELECT `totalIncome` FROM `t_doctor_account` WHERE `doctorId`=?";
	private static final String SELECT_ACCOUNT_DETAIL = "SELECT * FROM `t_doctor_account_detail` WHERE `doctorId`=? ORDER BY `date` DESC limit ?,?";
	private static final String SELECT_WITHDRAW = "SELECT * FROM `t_doctor_withdraw` WHERE `doctorId`=?";

	private static final String INSERT_WITHDRAW = "INSERT `t_doctor_withdraw`(`doctorId`,`targetType`,`target`,`targetName`,`money`,`bankId`,`accountCity`,`beginDate`,`status`) VALUES(?,?,?,?,?,?,?,?,?);";
	private static final String INSERT_ACCOUNT = "INSERT `t_doctor_account`(`doctorId`,`balance`,`coming`,`totalIncome`,`totalWithdraw`) VALUES(?,?,?,?,?);";
	private static final String INSERT_ACCOUNT_DETAIL = "INSERT `t_doctor_account_detail`(`doctorId`,`src`,`srcType`,`type`,`money`) VALUE(?,?,?,?,?)";

	private static final String UPDATE_PASSWORD = "UPDATE `t_doctor_account` SET `password`=? WHERE `doctorId`=?";
	private static final String INC_BALACNE = "UPDATE `t_doctor_account` SET `balance`=`balance`+? WHERE `doctorId`=?";
	private static final String INC_COMING = "UPDATE `t_doctor_account` SET `coming`=`coming`+? WHERE `doctorId`=?";
	private static final String INC_TOTAL_INCOME = "UPDATE `t_doctor_account` SET `totalIncome`=`totalIncome`+? WHERE `doctorId`=?";
	private static final String INC_TOTAL_WITHDRAW = "UPDATE `t_doctor_account` SET `totalWithdraw`=`totalWithdraw`+? WHERE `doctorId`=?";
	private static final String COMING_TO_BALANCE = "UPDATE `t_doctor_account` SET `balance`=`balance`+?,`coming`=`coming`-? WHERE `doctorId`=?";

	protected AccountInfo loadAccountInfo(Doctor doctor) {
		return queryForObject(SELECT_ACCOUNT, new Object[] { doctor.getId() }, AccountInfo.builder);
	}

	protected List<AccountDetail> loadAccountDetail(Doctor doctor, int start, int pageSize) {
		return jdbc.query(SELECT_ACCOUNT_DETAIL, new Object[] { doctor.getId(), start, pageSize }, AccountDetail.builder);
	}

	protected String getPassword(Doctor doctor) {
		return queryForString(SELECT_PASSWORD, new Object[] { doctor.getId() });
	}

	protected void setPassword(Doctor doctor, String password) {
		jdbc.update(UPDATE_PASSWORD, new Object[] { password, doctor.getId() });
	}

	/** 余额 */
	protected BigDecimal getBalance(Doctor doctor) {
		BigDecimal balance = queryForObject(SELECT_BALANCE, new Object[] { doctor.getId() }, BigDecimal.class);
		return balance == null ? new BigDecimal(0) : balance;
	}

	/** 总收入 */
	protected BigDecimal getTotalIncome(Doctor doctor) {
		return queryForObject(SELECT_TOTAL_INCOME, new Object[] { doctor.getId() }, BigDecimal.class);
	}

	/** 添加提现申请 */
	protected long addWithdraw(final Doctor doctor, final Withdraw withdraw) {
		KeyHolder holder = new GeneratedKeyHolder();
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_WITHDRAW, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, doctor.getId());
				ps.setInt(2, withdraw.getTargetType());
				ps.setString(3, withdraw.getTarget());
				ps.setString(4, withdraw.getTargetName());
				ps.setBigDecimal(5, withdraw.getMoney());
				ps.setInt(6, withdraw.getBankId());
				ps.setString(7, withdraw.getAccountCity());
				ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
				ps.setInt(9, WithdrawStatus.PROCCESSING.ordinal());
				return ps;
			}
		};
		jdbc.update(psc, holder);
		long id = holder.getKey().longValue();
		return id;

	}

	/** 增加余额 */
	protected void incBalance(Doctor doctor, BigDecimal incBalance) {
		jdbc.update(INC_BALACNE, new Object[] { incBalance, doctor.getId() });
	}

	/** 增加即将到账 */
	protected void incComing(Doctor doctor, BigDecimal incComing) {
		jdbc.update(INC_COMING, new Object[] { incComing, doctor.getId() });
	}

	/** 即将到账转给余额 */
	protected void coming2Balance(Doctor doctor, BigDecimal transfer) {
		jdbc.update(COMING_TO_BALANCE, new Object[] { transfer, transfer, doctor.getId() });
	}

	/** 增加总收入 */
	protected void incTotalIncome(Doctor doctor, BigDecimal incBalance) {
		jdbc.update(INC_TOTAL_INCOME, new Object[] { incBalance, doctor.getId() });
	}

	/** 增加总提现金额 */
	protected void incTotalWithdraw(Doctor doctor, BigDecimal incBalance) {
		jdbc.update(INC_TOTAL_WITHDRAW, new Object[] { incBalance, doctor.getId() });
	}

	/** 减少余额 */
	protected void decBalance(Doctor doctor, BigDecimal incBalance) {
		jdbc.update(INC_BALACNE, new Object[] { incBalance.negate(), doctor.getId() });
	}

	/** 新开一个账户 */
	protected void newAccount(Doctor doctor) {
		jdbc.update(INSERT_ACCOUNT, new Object[] { doctor.getId(), 0, 0, 0, 0 });
	}

	protected List<Withdraw> withdrawHistory(Doctor doctor) {
		return jdbc.query(SELECT_WITHDRAW, new Object[] { doctor.getId() }, Withdraw.builder);
	}

	protected void addAccountDetail(Doctor doctor, AccountDetail accountDetail) {
		jdbc.update(INSERT_ACCOUNT_DETAIL, new Object[] { accountDetail.getDoctorId(), accountDetail.getSrc(), accountDetail.getSrcType(), accountDetail.getType(), accountDetail.getMoney() });
	}
}
