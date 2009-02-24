/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.miv.graphstream.tool.workbench.event;

import org.miv.graphstream.tool.workbench.Context;
import java.util.EventObject;

/**
 * Defines a context change event.
 * This event contains the new context and the object
 * which has fired this event.
 * 
 * @see org.miv.graphstream.tool.workbench.Context
 * @see java.util.EventObject
 * 
 * @author Guilhelm Savin
 *
 */
public class ContextEvent extends EventObject
{
	/**
	 * Defines serial version uid.
	 */
	public static final long serialVersionUID = 0x00A001L;
	
	/**
	 * New context.
	 */
	protected Context ctx;
	
	/**
	 * Build a new ContextChangeEvent.
	 * 
	 * @param source object which fired this event
	 * @param ctx new context
	 */
	public ContextEvent( Object source, Context ctx )
	{
		super( source );
		
		this.ctx = ctx;
	}
	
	/**
	 * Get the new context.
	 * 
	 * @return new context
	 */
	public Context getContext()
	{
		return this.ctx;
	}
}
