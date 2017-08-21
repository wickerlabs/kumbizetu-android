package com.roomorama.caldroid;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.caldroid.R;
import com.hirondelle.date4j.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The CaldroidGridAdapter provides customized view for the dates gridview
 *
 * @author thomasdao
 */
public class CaldroidGridAdapter extends BaseAdapter {
    protected ArrayList<DateTime> datetimeList;
    protected int month;
    protected int year;
    protected Context context;
    protected ArrayList<DateTime> disableDates;
    protected ArrayList<DateTime> selectedDates;

    // Use internally, to make the search for date faster instead of using
    // indexOf methods on ArrayList
    protected Map<DateTime, Integer> disableDatesMap = new HashMap<>();
    protected LinkedHashMap<DateTime, Integer> selectedDatesMap = new LinkedHashMap<>();

    protected DateTime minDateTime;
    protected DateTime maxDateTime;
    protected DateTime today;
    protected int startDayOfWeek;
    protected boolean sixWeeksInCalendar;
    protected boolean squareTextViewCell;
    protected int themeResource;
    protected Resources resources;

    protected int defaultCellBackgroundRes = -1;
    protected ColorStateList defaultTextColorRes;

    /**
     * caldroidData belongs to Caldroid
     */
    protected Map<String, Object> caldroidData;
    /**
     * extraData belongs to client
     */
    protected Map<String, Object> extraData;

    protected LayoutInflater localInflater;

    /**
     * Constructor
     *
     * @param context
     * @param month
     * @param year
     * @param caldroidData
     * @param extraData
     */
    public CaldroidGridAdapter(Context context, int month, int year,
                               Map<String, Object> caldroidData,
                               Map<String, Object> extraData) {
        super();
        this.month = month;
        this.year = year;
        this.context = context;
        this.caldroidData = caldroidData;
        this.extraData = extraData;
        this.resources = context.getResources();

        // Get data from caldroidData
        populateFromCaldroidData();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        localInflater = CaldroidFragment.getThemeInflater(context, inflater, themeResource);
    }

    public void setAdapterDateTime(DateTime dateTime) {
        this.month = dateTime.getMonth();
        this.year = dateTime.getYear();
        this.datetimeList = CalendarHelper.getFullWeeks(this.month, this.year,
                startDayOfWeek, sixWeeksInCalendar);
    }

    // GETTERS AND SETTERS
    public ArrayList<DateTime> getDatetimeList() {
        return datetimeList;
    }

    public DateTime getMinDateTime() {
        return minDateTime;
    }

    public void setMinDateTime(DateTime minDateTime) {
        this.minDateTime = minDateTime;
    }

    public DateTime getMaxDateTime() {
        return maxDateTime;
    }

    public void setMaxDateTime(DateTime maxDateTime) {
        this.maxDateTime = maxDateTime;
    }

    public ArrayList<DateTime> getDisableDates() {
        return disableDates;
    }

    public void setDisableDates(ArrayList<DateTime> disableDates) {
        this.disableDates = disableDates;
    }

    public ArrayList<DateTime> getSelectedDates() {
        return selectedDates;
    }

    public void setSelectedDates(ArrayList<DateTime> selectedDates) {
        this.selectedDates = selectedDates;
    }

    public int getThemeResource() {
        return themeResource;
    }

    public Map<String, Object> getCaldroidData() {
        return caldroidData;
    }

    public void setCaldroidData(Map<String, Object> caldroidData) {
        this.caldroidData = caldroidData;

        // Reset parameters
        populateFromCaldroidData();
    }

    public Map<String, Object> getExtraData() {
        return extraData;
    }

    public void setExtraData(Map<String, Object> extraData) {
        this.extraData = extraData;
    }

    /**
     * Retrieve internal parameters from caldroid data
     */
    @SuppressWarnings("unchecked")
    private void populateFromCaldroidData() {
        disableDates = (ArrayList<DateTime>) caldroidData
                .get(CaldroidFragment.DISABLE_DATES);
        if (disableDates != null) {
            disableDatesMap.clear();
            for (DateTime dateTime : disableDates) {
                disableDatesMap.put(dateTime, 1);
            }
        }

        selectedDates = (ArrayList<DateTime>) caldroidData
                .get(CaldroidFragment.SELECTED_DATES);
        if (selectedDates != null) {
            selectedDatesMap.clear();
            for (DateTime dateTime : selectedDates) {
                selectedDatesMap.put(dateTime, 1);
            }
            selectedDatesMap = sortByKeys(selectedDatesMap);
        }

        minDateTime = (DateTime) caldroidData
                .get(CaldroidFragment._MIN_DATE_TIME);
        maxDateTime = (DateTime) caldroidData
                .get(CaldroidFragment._MAX_DATE_TIME);
        startDayOfWeek = (Integer) caldroidData
                .get(CaldroidFragment.START_DAY_OF_WEEK);
        sixWeeksInCalendar = (Boolean) caldroidData
                .get(CaldroidFragment.SIX_WEEKS_IN_CALENDAR);
        squareTextViewCell = (Boolean) caldroidData
                .get(CaldroidFragment.SQUARE_TEXT_VIEW_CELL);

        // Get theme
        themeResource = (Integer) caldroidData
                .get(CaldroidFragment.THEME_RESOURCE);

        this.datetimeList = CalendarHelper.getFullWeeks(this.month, this.year,
                startDayOfWeek, sixWeeksInCalendar);

        getDefaultResources();
    }

    public DateTime getStartSelectedDate() {
        if (!selectedDatesMap.isEmpty()) {
            return getKeyByValue(selectedDatesMap, 0);
        }
        return null;
    }

    public boolean isSingleSelectedDate() {
        return (!selectedDatesMap.isEmpty() && selectedDatesMap.size() == 1);
    }

    public DateTime getEndSelectedDate() {
        if (!selectedDatesMap.isEmpty()) {
            return getKeyByValue(selectedDatesMap, selectedDatesMap.size() - 1);
        }
        return null;
    }

    private <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public <K extends Comparable, V extends Comparable> LinkedHashMap<K, V> sortByKeys(LinkedHashMap<K, V> map) {
        List<K> keys = new LinkedList<>(map.keySet());
        Collections.sort(keys, (Comparator<? super K>) new Comparator<DateTime>() {
            @Override
            public int compare(DateTime first, DateTime second) {
                //Collator collator = Collator.getInstance(new Locale("tr", "TR"));
                return first.compareTo(second);
            }
        });

        LinkedHashMap<K, V> sortedMap = new LinkedHashMap<>();
        Integer index = new Integer(0);
        for (K key : keys) {
            sortedMap.put(key, (V) index);
            index++;
//            sortedMap.put(key, map.get(key));
        }

        return sortedMap;
    }

    // This method retrieve default resources for background and text color,
    // based on the Caldroid theme
    private void getDefaultResources() {
        Context wrapped = new ContextThemeWrapper(context, themeResource);

        // Get style of normal cell or square cell in the theme
        Resources.Theme theme = wrapped.getTheme();
        TypedValue styleCellVal = new TypedValue();
        if (squareTextViewCell) {
            theme.resolveAttribute(R.attr.styleCaldroidSquareCell, styleCellVal, true);
        } else {
            theme.resolveAttribute(R.attr.styleCaldroidNormalCell, styleCellVal, true);
        }

        // Get default background of cell
        TypedArray typedArray = wrapped.obtainStyledAttributes(styleCellVal.data, R.styleable.Cell);
        defaultCellBackgroundRes = typedArray.getResourceId(R.styleable.Cell_android_background, -1);
        defaultTextColorRes = typedArray.getColorStateList(R.styleable.Cell_android_textColor);
        typedArray.recycle();
    }

    public void updateToday() {
        today = CalendarHelper.convertDateToDateTime(new Date());
    }

    protected DateTime getToday() {
        if (today == null) {
            today = CalendarHelper.convertDateToDateTime(new Date());
        }
        return today;
    }

    @SuppressWarnings("unchecked")
    protected void setCustomResources(DateTime dateTime, View backgroundView,
                                      TextView textView) {
        // Set custom background resource
        Map<DateTime, Drawable> backgroundForDateTimeMap = (Map<DateTime, Drawable>) caldroidData
                .get(CaldroidFragment._BACKGROUND_FOR_DATETIME_MAP);
        if (backgroundForDateTimeMap != null) {
            // Get background resource for the dateTime
            Drawable drawable = backgroundForDateTimeMap.get(dateTime);

            // Set it
            if (drawable != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    backgroundView.setBackground(drawable);
                } else {
                    backgroundView.setBackgroundDrawable(drawable);
                }
            }
        }

        // Set custom text color
        Map<DateTime, Integer> textColorForDateTimeMap = (Map<DateTime, Integer>) caldroidData
                .get(CaldroidFragment._TEXT_COLOR_FOR_DATETIME_MAP);
        if (textColorForDateTimeMap != null) {
            // Get textColor for the dateTime
            Integer textColorResource = textColorForDateTimeMap.get(dateTime);

            // Set it
            if (textColorResource != null) {
                textView.setTextColor(ResourcesCompat.getColor(resources, textColorResource, null));
            }
        }
    }

    private void resetCustomResources(CellView cellView) {
        cellView.setBackgroundResource(defaultCellBackgroundRes);
        cellView.setTextColor(defaultTextColorRes);
    }

    /**
     * Customize colors of text and background based on states of the cell
     * (disabled, active, selected, etc)
     * <p/>
     * To be used only in getView method
     *
     * @param position
     * @param cellView
     */
    protected void customizeTextView(int position, CellView cellView) {
        // Get the padding of cell so that it can be restored later
        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);

        cellView.resetCustomStates();
        resetCustomResources(cellView);

        if (dateTime.equals(getToday())) {
            cellView.addCustomState(CellView.STATE_TODAY);
        }

        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {
            cellView.addCustomState(CellView.STATE_PREV_NEXT_MONTH);
        }

        // Customize for disabled dates and date outside min/max dates
        if ((minDateTime != null && dateTime.lt(minDateTime))
                || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDatesMap
                .containsKey(dateTime))) {

            cellView.addCustomState(CellView.STATE_DISABLED);
        }

        // Customize for selected dates
        if (selectedDates != null && selectedDatesMap.containsKey(dateTime)) {
            if (selectedDatesMap.size() == 1) {
                handleSingleSelectedState(cellView);
            } else {
                handleStartSelectedState(dateTime, cellView);
                handleMiddleSelectedState(dateTime, cellView);
                handleEndSelectedState(dateTime, cellView);
            }
        }

//        if (position==0){
//            cellView.addCustomState(CellView.STATE_SELECTED_START);
//        }

        cellView.refreshDrawableState();

        // Set text
        cellView.setText(String.valueOf(dateTime.getDay()));
        //cellView.setText(Utils.getLocalNumber(dateTime.getDay()));

        // Set custom color if required
        setCustomResources(dateTime, cellView, cellView);

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);
    }

    private void handleSingleSelectedState(CellView cellView) {
        cellView.addCustomState(CellView.STATE_SELECTED_SINGLE);
    }

    private void handleStartSelectedState(DateTime dateTime, CellView cellView) {
        if (selectedDatesMap.get(dateTime) == 0)
            cellView.addCustomState(CellView.STATE_SELECTED_START);
    }

    private void handleMiddleSelectedState(DateTime dateTime, CellView cellView) {
        if (selectedDatesMap.get(dateTime) != 0 && selectedDatesMap.get(dateTime) != selectedDatesMap.size() - 1)
            cellView.addCustomState(CellView.STATE_SELECTED);

    }

    private void handleEndSelectedState(DateTime dateTime, CellView cellView) {
        if (selectedDatesMap.get(dateTime) == selectedDatesMap.size() - 1)
            cellView.addCustomState(CellView.STATE_SELECTED_END);
    }


    @Override
    public int getCount() {
        return this.datetimeList.size();
    }

    @Override
    public Object getItem(int position) {
        return datetimeList.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CellView cellView;

        // For reuse
        if (convertView == null) {
            final int squareDateCellResource = squareTextViewCell ? R.layout.square_date_cell : R.layout.normal_date_cell;
            cellView = (CellView) localInflater.inflate(squareDateCellResource, parent, false);
        } else {
            cellView = (CellView) convertView;
        }

        customizeTextView(position, cellView);

        return cellView;
    }

}
