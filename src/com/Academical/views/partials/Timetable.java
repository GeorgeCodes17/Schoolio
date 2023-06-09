package com.Academical.views.partials;

import com.Academical.Launcher;
import com.Academical.api.LessonScheduleApi;
import com.Academical.exceptions.LessonScheduleException;
import com.Academical.objects.LessonScheduleObject;
import com.Academical.views.partials.helpers.Colors;
import com.Academical.views.partials.helpers.RoundedBorder;
import net.miginfocom.swing.MigLayout;
import org.apache.logging.log4j.Level;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.Iterator;

public class Timetable {
    private final Font headerFont = new Font("Roboto", Font.BOLD, 16);
    private final Font timetableFont = new Font("Roboto", Font.PLAIN, 14);
    private final LessonScheduleApi lessonScheduleApi = new LessonScheduleApi();

    private final JLabel noLessonsLabel = new JLabel("No lessons scheduled");

    public JPanel getTimetable() {
        JPanel timetable = new JPanel();
        timetable.setPreferredSize(new Dimension(300, timetable.getPreferredSize().height));
        timetable.setLayout(new MigLayout("fill", "[100%]", "[15%][85%]"));

        LessonScheduleObject[] lessonSchedule = null;
        try {
            lessonSchedule = lessonScheduleApi.index();
        } catch (LessonScheduleException e) {
            Launcher.logAll(Level.WARN, e);
            noLessonsLabel.setText("Failed to retrieve lesson schedule");
            noLessonsLabel.setForeground(Color.RED);
        }

        timetable.add(new LessonButton(), "align right, wrap");
        timetable.add(getTimetableBody(lessonSchedule), "grow");

        return timetable;
    }

    private JPanel getTimetableBody(LessonScheduleObject[] lessonSchedule) {
        JPanel timetableBody = new JPanel();
        timetableBody.setBorder(new RoundedBorder(15, Colors.DARK_LAVA));

        if (lessonSchedule == null) {
            noLessonsLabel.setBorder(new EmptyBorder(40,0,0,0));

            timetableBody.add(noLessonsLabel);
        } else {
            timetableBody.setLayout(new GridLayout(lessonSchedule.length + 1, 3));

            timetableBody.add(timetableEvent("Activity", true, true));
            timetableBody.add(timetableEvent("Year", true, true));
            timetableBody.add(timetableEvent("Start time", true, true));

            Iterator<LessonScheduleObject> lessonScheduleIterator = Arrays.stream(lessonSchedule).iterator();
            while (lessonScheduleIterator.hasNext()) {
                LessonScheduleObject currentLesson = lessonScheduleIterator.next();

                boolean bottomBorder = lessonScheduleIterator.hasNext();
                timetableBody.add(timetableEvent(currentLesson.getLesson().getName(), false, bottomBorder));
                timetableBody.add(timetableEvent(currentLesson.getLesson().getYear().getName(), false, bottomBorder));
                timetableBody.add(timetableEvent(currentLesson.getStart().toString(), false, bottomBorder));
            }
        }

        return timetableBody;
    }

    private JLabel timetableEvent(String itemText, boolean bold, boolean bottomBorder) {
        JLabel item = new JLabel(itemText, SwingConstants.CENTER);
        item.setFont(bold ? headerFont : timetableFont);
        if (bottomBorder) {
            item.setBorder(new MatteBorder(0, 0, 1, 0, Colors.DARK_LAVA));
        }
        item.setPreferredSize(new Dimension(100, item.getPreferredSize().height));

        return item;
    }
}
