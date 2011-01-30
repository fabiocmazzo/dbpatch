package org.jsoftware.config;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class Patch implements Serializable {
	
	private static final long serialVersionUID = 4178101927323891639L;
	public enum DbState {
		COMMITED, IN_PROGRES, NOT_AVAILABLE
	}
	
	private String name;
	private int statementCount = -1;
	private File file;
	private Date dbDate;
	private DbState dbState;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatementCount() {
		return statementCount;
	}
	public void setStatementCount(int statementCount) {
		this.statementCount = statementCount;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public void setDbDate(Date dbDate) {
		this.dbDate = dbDate;
	}
	public void setDbState(DbState dbState) {
		this.dbState = dbState;
	}
	public Date getDbDate() {
		return dbDate;
	}
	public DbState getDbState() {
		return dbState;
	}
	public boolean canApply() {
		return statementCount > 0 && dbState == DbState.NOT_AVAILABLE;
	}
	@Override
	public String toString() {
		return super.toString() + "-" + name;
	}
}