/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution 
 * 3.0 Unported License. for more details.
 * 
 * You should have received a copy of the the Creative Commons Attribution 
 * 3.0 Unported License along with Parallax. 
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */

package org.parallax3d.myapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.parallax3d.myapp.MyAnimation;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.Parallax;
import org.parallax3d.parallax.platforms.gwt.GwtParallax;
import org.parallax3d.parallax.platforms.gwt.GwtRenderingContext;

public class WebApp implements EntryPoint, Parallax.ParallaxListener
{
	public void onModuleLoad()
	{
		// Remove loading panel
		RootPanel.get("loading").getElement().removeFromParent();

		GwtParallax.init( this );
	}

	@Override
	public void onParallaxApplicationReady(Parallax instance)
	{
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

			@Override
			public void execute()
			{
				try
				{
					new GwtRenderingContext(RootPanel.get("content")).setAnimation(new MyAnimation());
				}
				catch (Throwable e)
				{
					Log.error("setRendering: Sorry, your browser doesn't seem to support WebGL", e);
				}
			}
		});
	}

}
