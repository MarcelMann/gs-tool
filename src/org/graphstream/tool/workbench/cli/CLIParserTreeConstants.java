/*
 * Copyright 2006 - 2011 
 *     Julien Baudry	<julien.baudry@graphstream-project.org>
 *     Antoine Dutot	<antoine.dutot@graphstream-project.org>
 *     Yoann Pigné		<yoann.pigne@graphstream-project.org>
 *     Guilhelm Savin	<guilhelm.savin@graphstream-project.org>
 * 
 * This file is part of GraphStream <http://graphstream-project.org>.
 * 
 * GraphStream is a library whose purpose is to handle static or dynamic
 * graph, create them from scratch, file or any source and display them.
 * 
 * This program is free software distributed under the terms of two licenses, the
 * CeCILL-C license that fits European law, and the GNU Lesser General Public
 * License. You can  use, modify and/ or redistribute the software under the terms
 * of the CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
 * URL <http://www.cecill.info> or under the terms of the GNU LGPL as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C and LGPL licenses and that you accept their terms.
 */
package org.graphstream.tool.workbench.cli;

public interface CLIParserTreeConstants
{
  public int JJTSTART = 0;
  public int JJTAXIOMS = 1;
  public int JJTREADCLASSNAME = 2;
  public int JJTREADID = 3;
  public int JJTREADINT = 4;
  public int JJTREADATTRIBUTES = 5;
  public int JJTAXIOMSSYSTEM = 6;
  public int JJTAXIOMSGRAPH = 7;
  public int JJTAXIOMGRAPHCREATION = 8;
  public int JJTAXIOMGRAPHREAD = 9;
  public int JJTAXIOMGRAPHDISPLAY = 10;
  public int JJTAXIOMSSTREAM = 11;
  public int JJTAXIOMSTREAMNEW = 12;
  public int JJTAXIOMSTREAMCONNECT = 13;
  public int JJTAXIOMSGENERATOR = 14;
  public int JJTAXIOMGENERATORBEGIN = 15;
  public int JJTAXIOMGENERATORSTEP = 16;
  public int JJTAXIOMGENERATOREND = 17;


  public String[] jjtNodeName = {
    "start",
    "axioms",
    "readClassname",
    "readID",
    "readInt",
    "readAttributes",
    "axiomsSystem",
    "axiomsGraph",
    "axiomGraphCreation",
    "axiomGraphRead",
    "axiomGraphDisplay",
    "axiomsStream",
    "axiomStreamNew",
    "axiomStreamConnect",
    "axiomsGenerator",
    "axiomGeneratorBegin",
    "axiomGeneratorStep",
    "axiomGeneratorEnd",
  };
}
/* JavaCC - OriginalChecksum=3fcb94a01f7121754db870ac6be96c29 (do not edit this line) */
