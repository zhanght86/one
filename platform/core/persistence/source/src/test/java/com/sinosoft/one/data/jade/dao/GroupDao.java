package com.sinosoft.one.data.jade.dao;

import com.sinosoft.one.data.jade.annotation.SQL;
import com.sinosoft.one.data.jade.model.Group;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * User: Chunliang.Han
 * Time: 12-9-4[下午5:45]
 */
public interface GroupDao extends CrudRepository<Group, String> {
    //1.3.1
    @SQL("insert into t_code_group values(?1,?2)")
    void addGroupWithAnnoUseMultiParam(String id,String name);

    //1.3.2
    @SQL("insert into t_code_group values(?1.id,?1.name)")
    void addBatchGroupWithAnnoUseEntityParam(List<Group> group);

    //2.3.1
    @SQL("update t_code_group set name=?2 " +
            "where id=?1")
    void updateGroupWithAnnoUseEntityParam(String id,String name);

    //2.3.2
    @SQL("update t_code_group set name=?1.name " +
            "where id=?1.id")
    void updateBatchGroupWithAnnoUseEntityParam(List<Group> group);

    //3.3.1
    @SQL("delete from t_code_group where id=?1")
    void deleteGroupWithAnnoById(String id);

    //3.3.2
    @SQL("delete from t_code_group where id=?1.id")
    void deleteBatchGroupWithAnnoUseEntityParam(List<Group> group);

    @SQL("select count(1) from t_code_group")
    int countGroup();

    @SQL("select * from t_code_group")
    List<Group> selectAllGroup();

    @SQL("delete from t_code_group")
    void deleteAllGroup();
}