/*
 * 文件名称:          IWord.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:56:16
 */
package com.hunglv.office.simpletext.control;

import com.hunglv.office.common.shape.IShape;
import com.hunglv.office.java.awt.Rectangle;
import com.hunglv.office.pg.animate.IAnimation;
import com.hunglv.office.simpletext.model.IDocument;
import com.hunglv.office.system.IControl;

public interface IWord
{
    /**
     * @return Returns the highlight.
     */
    IHighlight getHighlight();
    /**
     * 
     */
    Rectangle modelToView(long offset, Rectangle rect, boolean isBack);
    /**
     * 
     */
    IDocument getDocument();
    /**
     * 
     */
    String getText(long start, long end);
    
    /**
     * @param x 100%
     * @param y 100%
     */
    long viewToModel(int x, int y, boolean isBack);
    /**
     * 
     */
    byte getEditType();
    
    /**
     * 
     * @param paragraphID
     * @return
     */
    IAnimation getParagraphAnimation(int paragraphID);
    
    /**
     * 
     */
    IShape getTextBox();
    
    /**
     * 
     */
    IControl getControl();
    
    /**
     * 
     */
    void dispose();
}
