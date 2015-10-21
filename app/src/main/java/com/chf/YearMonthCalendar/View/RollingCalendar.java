package com.chf.YearMonthCalendar.View;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chf.YearMonthCalendar.R;
import com.chf.YearMonthCalendar.Utils.StringUtil;

/**
 * -------日历控件-------
 * 
 * 获取到系统时间 getTime(); 获取系统默认的时间，格式{YYYYMM} getSystemTolerantTime()
 * 获取当前日历控件显示的时间，格式{YYYYMM} getSelectedTime()
 * 
 * @author chf
 * 
 */
public class RollingCalendar extends RelativeLayout implements OnClickListener,
		OnCheckedChangeListener {

	private Context mContext;
	private RadioGroup main_radio;// 月份显示控件
	private int mYear;// 获取到的系统的年份
	private int mMonth;// 获取到的系统的月份
	private String str;
	RadioButton child;

	private ImageButton ib_left;// 上年
	private ImageButton ib_right;// 下年
	private TextView text_year;// 年份显示view
	private int mostMonth;
	private int mCheckYear;
	private int mCheckMonth;
	private Calendar mCalendar;

	private int count = 5;

	public RollingCalendar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initView();
	}

	public RollingCalendar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	public RollingCalendar(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	private void initTime() {
		getTime();
		mostMonth = 12;
		mCheckYear = mYear;
		mCheckMonth = mMonth;
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		initTime();
		LayoutInflater.from(mContext).inflate(
				R.layout.view_rollingcalendar_view, this);
		main_radio = (RadioGroup) findViewById(R.id.main_radio);
		ib_left = (ImageButton) findViewById(R.id.ib_left);
		ib_right = (ImageButton) findViewById(R.id.ib_right);
		text_year = (TextView) findViewById(R.id.text_year);

		text_year.setText(mYear + "");
		ib_right.setEnabled(false);

		// 设置按钮的点击事件
		ib_left.setOnClickListener(this);
		ib_right.setOnClickListener(this);

		// 动态生成月份
		for (int i = 1; i <= mostMonth; i++) {
			View v1 = LayoutInflater.from(getContext()).inflate(
					R.layout.item_reconciliation_month_weight, main_radio,
					false);
			child = (RadioButton) v1.findViewById(R.id.month_number);
			child.setGravity(Gravity.CENTER);
			if (mMonth < i) {
				child.setEnabled(false);
			}
			final int id = i;
			child.setId(id);
			child.setText(i + "月");
			child.setTextSize(17);

			main_radio.addView(v1);
		}
		main_radio.check(mMonth);
		main_radio.setOnCheckedChangeListener(this);
		// tv_month_content.setText(mCheckMonth + "月的查询结果");
	}

	/**
	 * 设置全局的点击事件
	 */
	@Override
	public void onClick(View v) {
		str = (String) text_year.getText();
		mCheckYear = Integer.parseInt(str);

		switch (v.getId()) {
		// 点击上一年按钮
		case R.id.ib_left:
			if (count != 0) {
				count--;
				ib_left.setEnabled(true);
				--mCheckYear;
				text_year.setText(String.valueOf(mCheckYear));
				if (mCheckMonth != mostMonth) {
					mCheckMonth = mostMonth;
					childAt = (RadioButton) main_radio
							.getChildAt(mCheckMonth - 1);
					childAt.setChecked(true);
				} else {// 不会走系统回调
					changeMonthEnable(mCheckYear, mCheckMonth);
				}
				if (mCheckYear != mYear) {
					ib_right.setEnabled(true);
				}
			} else {
				ib_left.setEnabled(false);
			}
			break;

		// 点击下一年按钮
		case R.id.ib_right:
			if (count >= 0) {
				count++;
				ib_left.setEnabled(true);
				if (mCheckYear < mYear) {
					++mCheckYear;
					text_year.setText(String.valueOf(mCheckYear));
					if (mCheckYear != mYear) {
						changeMonthEnable(mCheckYear, mCheckMonth);
					} else {
						if (mCheckMonth <= mMonth) {
							changeMonthEnable(mCheckYear, mCheckMonth);
						} else {
							RadioButton childAt = (RadioButton) main_radio
									.getChildAt(mMonth - 1);
							childAt.setChecked(true);
						}
					}
				}
				if (mCheckYear == mYear) {
					ib_right.setEnabled(false);
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 获取到系统时间
	 */
	public String getTime() {
		mCalendar = Calendar.getInstance();
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH) + 1;
		return String.valueOf(mYear);
	}

	// 点击选择不同月份
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		mCheckMonth = checkedId;
		changeMonthEnable(mCheckYear, checkedId);
		// tv_month_content.setText(mCheckMonth + "月的查询结果");
	}

	// 选中月份的逻辑
	void changeMonthEnable(int checkedYear, int month) {
		if (checkedYear == mYear) {
			for (int i = 0; i < 12; i++) {
				main_radio.getChildAt(i).setEnabled(i < mMonth);
			}
		} else {
			for (int i = 0; i < 12; i++) {
				main_radio.getChildAt(i).setEnabled(i + 1 != month);
			}
		}
		if (mCallBack != null) {
			String month1 = null;
			if (month < 10) {
				month1 = "0" + String.valueOf(month);
			} else {
				month1 = String.valueOf(month);
			}
			mCallBack.onCheckedChanged(mCheckYear, month, mCheckYear + ""
					+ month1);
		}
	}

	// -------回调--------

	private RollingCalendarCallBack mCallBack;
	private RadioButton childAt;

	public void setRollingCalendarCallBack(RollingCalendarCallBack callBack) {
		this.mCallBack = callBack;
	}

	/**
	 * 
	 * @author chf
	 * 
	 * @param cYear
	 *            点击的年 int
	 * @param cMonth
	 *            点击的月 int
	 * @param yearMonth
	 *            点击的年+月字符串 String
	 */
	public interface RollingCalendarCallBack {
		void onCheckedChanged(int cYear, int cMonth, String yearMonth);
	}

	/**
	 * 获取当前日历控件显示的时间，格式{YYYYMM}
	 * 
	 * @return
	 */
	public String getSelectedTime() {
		return FormateCalendarDate();
	}

	/**
	 * 获取系统默认的时间，格式{YYYYMM}
	 * 
	 * @return
	 */
	public String getSystemTolerantTime() {
		String month = null;
		if (mMonth < 10) {
			month = "0" + String.valueOf(mMonth);
		}
		return String.valueOf(mYear) + month;
	}

	/**
	 * 格式化时间,月份个位数补零
	 * 
	 * @return 格式化月份后的时间
	 */
	public String FormateCalendarDate() {
		String month = null;
		if (mCheckMonth < 10) {
			month = "0" + String.valueOf(mMonth);
		} else {
			month = String.valueOf(mMonth);
		}
		String yearText = (String) text_year.getText();
		return StringUtil.isEmptyOrNull(yearText) ? String.valueOf(mYear)
				+ month : yearText + month;
	}

	/**
	 * 设置时间变化的回调
	 * 
	 * @author chf
	 * 
	 */
	public interface onDateChangedListener {
		void setYear(int year);

		void setMonth(int month);
	}

	public void setYear(int year) {
		text_year.setText(year);
	}

	public void setMonth(int month) {
		main_radio.getChildAt(month).setClickable(true);
	}
}
