package com.hotstepper13.alexa.gira.beans;

import java.util.List;

public class HueStateChange {

  private boolean on;
  private int bri;
  private int hue;
  private int sat;
  private String effect;
  private int ct;
  private String alert;
  private List<Double> xy;
  private int transitiontime;
  private int bri_inc;
  private int hue_inc;
  private int sat_inc;
  private List<Double> xy_inc;
  private int ct_inc;

  public HueStateChange(boolean on, int bri, int hue, int sat, String effect, int ct, String alert, List<Double> xy,
			int transitiontime, int bri_inc, int hue_inc, int sat_inc, List<Double> xy_inc, int ct_inc) {
		super();
		this.on = on;
		this.bri = bri;
		this.hue = hue;
		this.sat = sat;
		this.effect = effect;
		this.ct = ct;
		this.alert = alert;
		this.xy = xy;
		this.transitiontime = transitiontime;
		this.bri_inc = bri_inc;
		this.hue_inc = hue_inc;
		this.sat_inc = sat_inc;
		this.xy_inc = xy_inc;
		this.ct_inc = ct_inc;
	}

	public boolean isOn() {
		return on;
	}

	public int getBri() {
		return bri;
	}

	public int getHue() {
		return hue;
	}

	public int getSat() {
		return sat;
	}

	public String getEffect() {
		return effect;
	}

	public int getCt() {
		return ct;
	}

	public String getAlert() {
		return alert;
	}

	public List<Double> getXy() {
		return xy;
	}

	public int getTransitiontime() {
		return transitiontime;
	}

	public int getBri_inc() {
		return bri_inc;
	}

	public int getHue_inc() {
		return hue_inc;
	}

	public int getSat_inc() {
		return sat_inc;
	}

	public List<Double> getXy_inc() {
		return xy_inc;
	}

	public int getCt_inc() {
		return ct_inc;
	}

	@Override
	public String toString() {
		return "HueStateChange [on=" + on + ", bri=" + bri + ", hue=" + hue + ", sat=" + sat + ", effect=" + effect
				+ ", ct=" + ct + ", alert=" + alert + ", xy=" + xy + ", transitiontime=" + transitiontime + ", bri_inc="
				+ bri_inc + ", hue_inc=" + hue_inc + ", sat_inc=" + sat_inc + ", xy_inc=" + xy_inc + ", ct_inc=" + ct_inc + "]";
	}
	


}
