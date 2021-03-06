package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
@Component
public class JDBCTransfersDAO implements TransfersDAO {

	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCTransfersDAO(JdbcTemplate jdbcTemplate, AccountsDAO accountDAO) {
		this.jdbcTemplate = jdbcTemplate;
		this.accountDAO = accountDAO;
	}
	
	AccountsDAO accountDAO;
	
	@Override
	public List<Transfers> getAllTransfers(String username) {
		List<Transfers> listOfTransfers = new ArrayList<>();
		String sql = "select * from transfers inner join accounts on transfers.account_from = accounts.account_id inner join users on accounts.user_id = users.user_id where users.username = ?";
		
		 SqlRowSet results = jdbcTemplate.queryForRowSet(sql,username);
	        while(results.next()) {
	            Transfers transfer = mapRowToTransfers(results);
	            listOfTransfers.add(transfer);
	        }
		return listOfTransfers;
	}

	@Override
	public List<Transfers> getAllTransfersTo() {
	
		//String sql = ""
		
		return null;
	}

	@Override
	public List<Transfers> getAllTransfersFrom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String create(Long sender, Long receiver, double amount) {
		if (sender == receiver) {
			//	return "Sender & receiver cannot be the same";
		}
		
		Accounts account = new Accounts();
		account = accountDAO.findAccountByAccountId(sender);
		double balance = 0;
		balance = account.getBalance();
		if (balance > amount) {
			String sendTransfer = "insert into transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount values (2,2,?,?,?)";
			jdbcTemplate.update(sendTransfer, sender, receiver, amount);
			
			accountDAO.addBalance(amount, receiver);
			accountDAO.subBalance(amount, sender);
			return "Transfer completed";
			
		} else {
			return "Not enough TEbucks for transfer";
		}
		
	
		
	
	}

	@Override
	public Transfers requestAmount(Long transferId, int accountTo, double amount) {
		// TODO Auto-generated method stub
		return null;
	}

	
//	@Override
//	public String createOld(Long accountFrom, Long accountTo,double amount) {
//		
//		
//		
//		String sql =  "insert into transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) values (2,2,?,?,?)";
//		
//		jdbcTemplate.update(sql, accountFrom, accountTo, amount);
//		
//		
////		if(results.next()) {
////			return mapRowToTransfers(results);
////		} else {
////			return null;
////		}
////		
//		accountDAO.addBalance(amount, accountTo);
//		accountDAO.subBalance(amount, accountFrom);
//		
//		
//		return "Transfer completed";
//		
//	}

	@Override
	public Transfers getTransferByTransferId(Long TransferId) {
    String sql = "select * from transfers where transfer_id = ?";
		
		SqlRowSet results =  jdbcTemplate.queryForRowSet(sql,TransferId);
		if(results.next()) {
			return mapRowToTransfers(results);
		} else {
		return null;
	}
	}

	@Override
	public Transfers getTransferType(Long transferId) {
	String sql = "select transfer_type_desc from transfer_types join transfers on transfer_types.transfer_type_id = transfers.transfer_id where transfer_id = ?" ;
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
		if(results.next()) {
			return mapRowToTransfers(results);
		} else {
			return null;
		}
	
	}


	@Override
	public Transfers getStatus(Long transferId) {
	String sql = "select transfer_status_desc from transfer_statuses join transfers on transfer_statuses.transfer_status_id = transfers.transfer_id where transfer_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
		if(results.next()) {
			return mapRowToTransfers(results);
		} else {
			return null;
		}
	}
	private Transfers mapRowToTransfers(SqlRowSet results) {
		Transfers transfersRow = new Transfers();
		transfersRow.setTransferId      (results.getLong("transfer_id"));
		transfersRow.setTransferTypeId  (results.getInt("transfer_type_id"));
		transfersRow.setTransferStatusId(results.getInt("transer_status_id"));
		transfersRow.setAccountFrom     (results.getLong("account_from"));
		transfersRow.setAccountTo       (results.getLong("account_to"));
		transfersRow.setAmount          (results.getDouble("amount"));
		transfersRow.setTransferType    (results.getString("transfer_type_desc"));
		transfersRow.setTransferStatus  (results.getString("transfer_status_desc"));
		return transfersRow;
		
	}

	@Override
	public String sendAmount(Long sender, Long receiver, double amount) {
		// TODO Auto-generated method stub
		return null;
	}

}
