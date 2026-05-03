package com.infy.dto;

import java.time.LocalDate;
//Write the necessary annotations to validate the fields
public class CardDTO {
	private Integer cardId;
	private String nameOnCard;
	private String cvv;
	
	private CardType cardType;
	private Integer customerId;

	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public String getNameOnCard() {
		return nameOnCard;
	}

	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	private LocalDate expiryDate;
	@Override
	public String toString() {
		return "CardDTO [cardId=" + cardId + ", nameOnCard=" + nameOnCard + ", cvv=" + cvv + ", expiryDate="
				+ expiryDate + ", cardType=" + cardType + ", customerId=" + customerId + "]";
	}


}
