package org.dao.declare;

import org.bean.UserStatistics;

public interface UserStatisticsDao {

	public UserStatistics getUserStatistics(String userName);

	public int logAccessUnit(String name, int unitNum);
}
