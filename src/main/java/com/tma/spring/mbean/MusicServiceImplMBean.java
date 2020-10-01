package com.tma.spring.mbean;

import java.util.List;

import com.tma.spring.entity.Music;

public interface MusicServiceImplMBean {
	void createRecord(Music music);

	List<Music> displayRecords();

	void updateRecord(Music music);

	void deleteRecord(Integer id);

	Music findRecordById(Integer id);
}
