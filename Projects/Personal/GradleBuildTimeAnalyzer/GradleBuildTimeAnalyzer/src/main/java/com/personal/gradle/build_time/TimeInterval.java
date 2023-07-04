package com.personal.gradle.build_time;

import com.utils.data_types.data_items.DataItem;
import com.utils.data_types.data_items.di_long.FactoryDataItemULong;
import com.utils.data_types.data_items.objects.FactoryDataItemObjectComparable;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.string.StrUtils;

public class TimeInterval implements TableRowData {

	private static final long serialVersionUID = -7656996560632876053L;

	static final TableColumnData[] COLUMNS = {
			new TableColumnData("Name", "Name", 0.3),
			new TableColumnData("Start Time", "StartTime", 0.1),
			new TableColumnData("End Time", "EndTime", 0.1),
			new TableColumnData("", "", 1)
	};

	@Override
	public DataItem<?>[] getTableViewDataItemArray() {
		return new DataItem<?>[] {
				FactoryDataItemObjectComparable.newInstance(name),
				FactoryDataItemULong.newInstance(startTime),
				FactoryDataItemULong.newInstance(endTime)
		};
	}

	private final String name;
	private final long startTime;
	private final long endTime;

	TimeInterval(
			final String name,
			final long startTime,
			final long endTime) {

		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public long getStartTime() {
		return startTime;
	}
}
