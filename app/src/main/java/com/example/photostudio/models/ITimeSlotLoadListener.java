package com.example.photostudio.models;

import java.util.List;
import com.example.photostudio.models.TimeSlot;
public interface ITimeSlotLoadListener {
    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotLoadEmpty();
}
