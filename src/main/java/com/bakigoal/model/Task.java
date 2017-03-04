package com.bakigoal.model;

import com.bakigoal.Util;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Task entity
 * Created by ilmir on 03.03.17.
 */
@Entity
public class Task {

  public static final String DATE_PATTERN = "dd-MM-YYYY";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String title;
  private String author;
  private String deadline;
  private String month;
  private Double price;

  public Task() {
  }

  public Task(String title, LocalDate deadline, Double price, String author) {
    this.title = title;
    this.deadline = deadline.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    this.price = price;
    this.author = author;
    this.month = Util.getCurrentMonth(deadline);
  }

  public String getMonth() {
    return month;
  }

  public void setMonth(String month) {
    this.month = month;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getDeadline() {
    return deadline;
  }

  public void setDeadline(String deadline) {
    this.deadline = deadline;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "Task{" +
        "title='" + title + '\'' +
        ", price=" + price +
        '}';
  }
}
