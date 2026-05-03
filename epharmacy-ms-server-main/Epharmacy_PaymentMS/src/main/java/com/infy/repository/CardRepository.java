package com.infy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.infy.entity.Card;

public interface CardRepository extends JpaRepository<Card,String>{

	@Query("Select c from Card c where c.customerId=:customerId")
  public List<Card> findByCustomerId(@Param("customerId") int customerId);

  @Query("Select c from Card c where c.cardId=:cardId")
  Optional<Card> findByCardId(@Param("cardId")String cardId);

  @Query("Select c from Card c where c.nameOnCard=:nameOnCard AND c.cvv=:cvv")
  public Optional<Card> findByNameandCVV(@Param("nameOnCard") String nameOnCard,@Param("cvv")String cvv);
}
