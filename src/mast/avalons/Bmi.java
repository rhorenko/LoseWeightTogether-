package mast.avalons;

import android.content.Context;

public class Bmi {
	// These values are the minimum to reach each level
	public final static float STARVATION = 0;
	public final static float SEVERELY_UNDERWEIGHT = 16;
	public final static float UNDERWEIGHT = 17;
	public final static float NORMAL = 18.5f;
	public final static float OVERWEIGHT =	25;
	public final static float OBESE_I = 30;
	public final static float OBESE_II = 35;
	public final static float OBESE_III = 40;
	
	public final static float IDEAL_BMI = 22;
	
	public final static float[] values = {STARVATION, SEVERELY_UNDERWEIGHT, UNDERWEIGHT, NORMAL, OVERWEIGHT, OBESE_I, OBESE_II, OBESE_III};
	public String[] labels;
	public final static int size = 8;
	
	private static Bmi instance;
	
	public Bmi (Context context) {
		labels = new String[] {
			getLevelName(STARVATION, context),
			getLevelName(SEVERELY_UNDERWEIGHT, context),
			getLevelName(UNDERWEIGHT, context),
			getLevelName(NORMAL, context),
			getLevelName(OVERWEIGHT, context),
			getLevelName(OBESE_I, context),
			getLevelName(OBESE_II, context),
			getLevelName(OBESE_III, context)
		};
	}
	
	public static Bmi getInstance(Context context) {
		if (instance == null) {
			instance = new Bmi(context);
		}
		return instance;
	}
	
	public static float calculateBMI(float weight, float height, boolean metricHeight, boolean metricWeight) {
		if (!metricHeight) {
			// Assumes height in inches
			height *= 0.0254;
		}
		
		if (!metricWeight) {
			weight *= 0.45359237;
		}
		
		return (float) (weight / (height * height));
	}
	
	public static float getWeight(float bmi, float height, boolean metricHeight, boolean metricWeight) {
		if (!metricHeight) {
			height *= 0.0254;
		}
		
		float weight = (float) (bmi * height * height);
		
		if (!metricWeight) {
			weight /= 0.45359237;
		}
		
		return weight;
	}
	
	public static String getLevelName(float bmi, Context context) {
		if (bmi < SEVERELY_UNDERWEIGHT)
			return context.getString(R.string.starvation);
		if (bmi < UNDERWEIGHT)
			return context.getString(R.string.severely_underweight);
		if (bmi < NORMAL)
			return context.getString(R.string.underweight);
		if (bmi < OVERWEIGHT)
			return context.getString(R.string.normal);
		if (bmi < OBESE_I)
			return context.getString(R.string.overweight);
		if (bmi < OBESE_II)
			return context.getString(R.string.obese_class_i);
		if (bmi < OBESE_III)
			return context.getString(R.string.obese_class_ii);
		return context.getString(R.string.obese_class_iii);
	}
	
	public static float getIdealWeight(float height, boolean metricHeight, boolean metricWeight) {
		return getWeight(IDEAL_BMI, height, metricHeight, metricWeight);
	}
}
