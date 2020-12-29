package learn.tcc.exchange.api.mapper;

import learn.tcc.exchange.api.domain.dto.AccountDTO;
import org.apache.ibatis.annotations.Update;

public interface CnyAccountMapper {

    /**
     * increase cny
     *
     * @param accountDTO the accountDTO
     * @return int
     */
    @Update("update cny_account set balance = balance + #{amount}, " +
            "increase_freeze = increase_freeze - #{amount}, update_time = now() " +
            "where user_id = #{userId}")
    int increase(AccountDTO accountDTO);

    /**
     * increaseConfirm
     *
     * @param accountDTO the account dto
     * @return the int
     */
    @Update("update cny_account set " +
            "increase_freeze = increase_freeze + #{amount} " +
            "where user_id = #{userId}")
    int increaseConfirm(AccountDTO accountDTO);

    /**
     * increaseCancel
     *
     * @param accountDTO the account dto
     * @return the int
     */
    @Update("update cny_account set balance = balance - #{amount}, " +
            "increase_freeze = increase_freeze + #{amount} " +
            "where user_id = #{userId}")
    int increaseCancel(AccountDTO accountDTO);

    /**
     * decrease cny
     *
     * @param accountDTO the accountDTO
     * @return int
     */
    @Update("update cny_account set balance = balance - #{amount}, " +
            "decrease_freeze = decrease_freeze + #{amount}, update_time = now() " +
            "where user_id = #{userId}")
    int decrease(AccountDTO accountDTO);

    /**
     * decreaseConfirm
     *
     * @param accountDTO the account dto
     * @return the int
     */
    @Update("update cny_account set " +
            "decrease_freeze = decrease_freeze - #{amount} " +
            "where user_id = #{userId}")
    int decreaseConfirm(AccountDTO accountDTO);

    /**
     * decreaseCancel
     *
     * @param accountDTO the account dto
     * @return the int
     */
    @Update("update cny_account set balance = balance + #{amount}, " +
            "decrease_freeze = decrease_freeze - #{amount} " +
            "where user_id = #{userId}")
    int decreaseCancel(AccountDTO accountDTO);

}
