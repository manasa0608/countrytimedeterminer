package com.hubspot.countrytimedeterminer.helpers;

import com.hubspot.countrytimedeterminer.resources.Partner;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class DateHelper {

  public boolean checkConsecutiveDatesCriteria(Partner partner) {
    List<String> dates = partner.getAvailableDates();

    List<LocalDate> localDateList = new ArrayList<>();
    for (String s : dates) {
      String[] data = s.split("-");
      Month m = Month.of(Integer.parseInt(data[1]));
      LocalDate localDate = LocalDate.of(Integer.parseInt(data[0]), m, Integer.parseInt(data[2]));
      localDateList.add(localDate);
    }
    Collections.sort(localDateList);
    for (int i = 0; i < localDateList.size() - 1; i++) {
      LocalDate date1 = localDateList.get(i);
      LocalDate date2 = localDateList.get(i + 1);
      if (date1.plusDays(1).equals(date2)) {
        return true;
      }
    }

    return false;
  }

  public String convertDateToStringFormat(Date date) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return dateFormat.format(date);
  }

  public Date determineEarliestDate(List<String> value) {
    List<Date> dates = new ArrayList<>();

    for (String a : value) {
      String[] data = a.split("-");
      Month m = Month.of(Integer.parseInt(data[1]));
      LocalDate localDate = LocalDate.of(Integer.parseInt(data[0]), m, Integer.parseInt(data[2]));
      Date date = java.sql.Date.valueOf(localDate);
      dates.add(date);
    }
    return Collections.min(dates);
  }
}
