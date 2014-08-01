<?xml version="1.0" encoding="ISO-8859-1" ?>
<!-- edited with XML Spy v4.4 U (http://www.xmlspy.com) by Stuart Moss (Daeja Image Systems) -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://http://tempuri.org/FnDocAnnoList.xsd"
   xmlns:sec="http://tempuri.org/FnSecurity.xsd" version="1.0">
   <xsl:output method="text" omit-xml-declaration="yes" />
   <xsl:strip-space elements="*" />
   <xsl:template match="/">
[VERSION]
XSLVERSION = 311160
<xsl:apply-templates select="FnDocAnnoList" />
      <xsl:text>&#10;</xsl:text>
      <!-- Sticky Note-->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_CLASSNAME='StickyNote']">
[NOTE]
TEXT = <xsl:call-template name="substitute">
            <xsl:with-param name="string" select="PropDesc/F_TEXT" />
            <xsl:with-param name="from" select="'&#xA;'" />
            <xsl:with-param name="to" select="'&lt;n&gt;'" />
         </xsl:call-template><xsl:text>&#10;</xsl:text>
X = <xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
Y = <xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
WIDTH = <xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
HEIGHT = <xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
TOOLTIP = <xsl:value-of select="PropDesc/@F_ORDINAL" /> &lt;type> (<xsl:value-of select="PropDesc/@F_NAME" />)<xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_WIDTH=<xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_HEIGHT=<xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
<xsl:choose>
  <xsl:when test="PropDesc/@F_FORECOLOR" >
      FILLCOLOR = <xsl:value-of select="PropDesc/@F_FORECOLOR" />
  </xsl:when>
  <xsl:otherwise>FILLCOLOR = 65535</xsl:otherwise>
</xsl:choose><xsl:text>&#10;</xsl:text>

RECTANGULAR=1
      <xsl:apply-templates />
      </xsl:for-each>
<!-- Proprietary Sticky Note-->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_SUBCLASS='v1-Note']">
[NOTE]
TEXT = <xsl:call-template name="substitute">
            <xsl:with-param name="string" select="PropDesc/F_TEXT" />
            <xsl:with-param name="from" select="'&#xA;'" />
            <xsl:with-param name="to" select="'&lt;n&gt;'" />
         </xsl:call-template><xsl:text>&#10;</xsl:text>
X = <xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
Y = <xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
WIDTH = <xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
HEIGHT = <xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
TOOLTIP = <xsl:value-of select="PropDesc/@F_ORDINAL" /> &lt;type> (<xsl:value-of select="PropDesc/@F_NAME" />)<xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_WIDTH=<xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_HEIGHT=<xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
<xsl:choose>
  <xsl:when test="PropDesc/@F_FORECOLOR" >
      FILLCOLOR = <xsl:value-of select="PropDesc/@F_FORECOLOR" />
  </xsl:when>
  <xsl:otherwise>FILLCOLOR = 65535</xsl:otherwise>
</xsl:choose><xsl:text>&#10;</xsl:text>
VIEW = <xsl:value-of select="PropDesc/@F_VIEWOPTION" /><xsl:text>&#10;</xsl:text>
RECTANGULAR=1
      <xsl:apply-templates />
      </xsl:for-each>

<!-- Highlight-->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_CLASSNAME='Highlight']">
[HIGHLIGHT]
X = <xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
Y = <xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
WIDTH = <xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
HEIGHT = <xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
COLOR = <xsl:value-of select="PropDesc/@F_LINE_COLOR" /><xsl:text>&#10;</xsl:text>
FILLCOLOR = <xsl:value-of select="PropDesc/@F_BRUSHCOLOR" /><xsl:text>&#10;</xsl:text>
         <xsl:choose>
            <xsl:when test="PropDesc/@F_TEXT_BACKMODE='1'">
TRANSPARENT = 1
</xsl:when>
            <xsl:otherwise>
TRANSPARENT = 1
</xsl:otherwise>
         </xsl:choose><xsl:text>&#10;</xsl:text>
LINEWIDTH = <xsl:value-of select="PropDesc/@F_LINE_WIDTH" /><xsl:text>&#10;</xsl:text>
TOOLTIP = <xsl:value-of select="PropDesc/@F_ORDINAL" /> &lt;type>  (<xsl:value-of select="PropDesc/@F_NAME" />)<xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_HEIGHT=<xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_WIDTH=<xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
         <xsl:apply-templates />
      </xsl:for-each>
      <!-- Oval-->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_SUBCLASS='v1-Oval']">
[OVAL]
X = <xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
Y = <xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
WIDTH = <xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
HEIGHT = <xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
COLOR = <xsl:value-of select="PropDesc/@F_LINE_COLOR" /><xsl:text>&#10;</xsl:text>
FILLCOLOR = <xsl:value-of select="PropDesc/@F_BRUSHCOLOR" /><xsl:text>&#10;</xsl:text>
         <xsl:choose>
            <xsl:when test="PropDesc/@F_TEXT_BACKMODE='1'">
TRANSPARENT = 1
</xsl:when>
            <xsl:otherwise>
TRANSPARENT = 0
</xsl:otherwise>
         </xsl:choose><xsl:text>&#10;</xsl:text>
LINEWIDTH = <xsl:value-of select="PropDesc/@F_LINE_WIDTH" /><xsl:text>&#10;</xsl:text>
TOOLTIP = <xsl:value-of select="PropDesc/@F_ORDINAL" /> &lt;type>  (<xsl:value-of select="PropDesc/@F_NAME" />)<xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_HEIGHT=<xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_WIDTH=<xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
         <xsl:apply-templates />
      </xsl:for-each>
      <!-- Rectangle-->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_SUBCLASS='v1-Rectangle']">
[RECTANGLE]
X = <xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
Y = <xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
WIDTH = <xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
HEIGHT = <xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
COLOR = <xsl:value-of select="PropDesc/@F_LINE_COLOR" /><xsl:text>&#10;</xsl:text>
FILLCOLOR = <xsl:value-of select="PropDesc/@F_BRUSHCOLOR" /><xsl:text>&#10;</xsl:text>
         <xsl:choose>
            <xsl:when test="PropDesc/@F_TEXT_BACKMODE='1'">
TRANSPARENT = 1
</xsl:when>
            <xsl:otherwise>
TRANSPARENT = 0
</xsl:otherwise>
         </xsl:choose><xsl:text>&#10;</xsl:text>
LINEWIDTH = <xsl:value-of select="PropDesc/@F_LINE_WIDTH" /><xsl:text>&#10;</xsl:text>
TOOLTIP = <xsl:value-of select="PropDesc/@F_ORDINAL" /> &lt;type>  (<xsl:value-of select="PropDesc/@F_NAME" />)<xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_HEIGHT=<xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_WIDTH=<xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
         <xsl:apply-templates />
      </xsl:for-each>
      <!-- Redact-->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_SUBCLASS='v1-Redaction']">
[REDACT]
X = <xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
Y = <xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
WIDTH = <xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
HEIGHT = <xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
COLOR = <xsl:value-of select="PropDesc/@F_LINE_COLOR" /><xsl:text>&#10;</xsl:text>
FILLCOLOR = <xsl:value-of select="PropDesc/@F_BRUSHCOLOR" /><xsl:text>&#10;</xsl:text>
TRANSPARENT = 0
LINEWIDTH = <xsl:value-of select="PropDesc/@F_LINE_WIDTH" /><xsl:text>&#10;</xsl:text>
TOOLTIP = <xsl:value-of select="PropDesc/@F_ORDINAL" /> &lt;type>  (<xsl:value-of select="PropDesc/@F_NAME" />)<xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_HEIGHT=<xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_WIDTH=<xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
         <xsl:apply-templates />
      </xsl:for-each>
      <!-- Stamp -->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_SUBCLASS='v1-Image Stamp']">
[STAMP]
FNSTAMP = 0
RESOURCE= <xsl:call-template name="substitute">
            <xsl:with-param name="string" select="PropDesc/F_TEXT" />
            <xsl:with-param name="from" select="'&#xA;'" />
            <xsl:with-param name="to" select="'&lt;n&gt;'" />
         </xsl:call-template><xsl:text>&#10;</xsl:text>
X = <xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
Y = <xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>


LINEWIDTH = <xsl:value-of select="PropDesc/@F_LINE_WIDTH" /><xsl:text>&#10;</xsl:text>

         <xsl:choose>
            <xsl:when test="PropDesc/@F_SCALE &gt; 0">

SCALE = <xsl:value-of select="PropDesc/@F_SCALE" /><xsl:text>&#10;</xsl:text>

</xsl:when>
            <xsl:otherwise>
WIDTH = <xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
HEIGHT = <xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>

</xsl:otherwise>
         </xsl:choose>

CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
         <xsl:apply-templates />
      </xsl:for-each>
      <!-- Text -->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_CLASSNAME='Text']">
[TEXT]
CUSTOMPROPERTY = F_CLASSNAME=Text
FNSTAMP = 0
TEXT = <xsl:call-template name="substitute">
            <xsl:with-param name="string" select="PropDesc/F_TEXT" />
            <xsl:with-param name="from" select="'&#xA;'" />
            <xsl:with-param name="to" select="'&lt;n&gt;'" />
         </xsl:call-template><xsl:text>&#10;</xsl:text>
X = <xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
Y = <xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
WIDTH = <xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
HEIGHT = <xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_WIDTH=<xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_HEIGHT=<xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
COLOR = <xsl:value-of select="PropDesc/@F_FORECOLOR" /><xsl:text>&#10;</xsl:text>
FONTHEIGHT = <xsl:value-of select="PropDesc/@F_FONT_SIZE" /><xsl:text>&#10;</xsl:text>
TEXTROTATION = <xsl:value-of select="PropDesc/@F_ROTATION" /><xsl:text>&#10;</xsl:text>
ROTATION = <xsl:value-of select="PropDesc/@F_IMAGEROTATION" /><xsl:text>&#10;</xsl:text>
<xsl:choose>
<xsl:when test="PropDesc/@F_FONT_STRIKETHROUGH='true'">STRIKETHROUGH = 1</xsl:when>
</xsl:choose><xsl:text>&#10;</xsl:text>

<xsl:choose>
<xsl:when test="PropDesc/@F_TEXT_ALIGNMENT='right'">ALIGNMENT = RIGHT</xsl:when>
<xsl:when test="PropDesc/@F_TEXT_ALIGNMENT='RIGHT'">ALIGNMENT = RIGHT</xsl:when>
</xsl:choose>

BORDER = <xsl:choose>
            <xsl:when test="PropDesc/@F_HASBORDER='true'">1</xsl:when>
            <xsl:otherwise>0</xsl:otherwise>
         </xsl:choose><xsl:text>&#10;</xsl:text>
BORDERCOLOR = <xsl:value-of select="PropDesc/@F_BORDER_COLOR" /><xsl:text>&#10;</xsl:text>
BORDERWIDTH = <xsl:value-of select="PropDesc/@F_BORDER_WIDTH" /><xsl:text>&#10;</xsl:text>
FILLCOLOR = <xsl:value-of select="PropDesc/@F_BACKCOLOR" /><xsl:text>&#10;</xsl:text>
TOOLTIP = <xsl:value-of select="PropDesc/@F_ORDINAL" /> &lt;type>  (<xsl:value-of select="PropDesc/@F_NAME" />)<xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_BORDER_BACKMODE=<xsl:value-of select="PropDesc/@F_BORDER_BACKMODE" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_BORDER_COLOR=<xsl:value-of select="PropDesc/@F_BORDER_COLOR" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_BORDER_STYLE=<xsl:value-of select="PropDesc/@F_BORDER_STYLE" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_FONT_BOLD=<xsl:value-of select="PropDesc/@F_FONT_BOLD" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_FONT_ITALIC=<xsl:value-of select="PropDesc/@F_FONT_ITALIC" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_FONT_NAME=<xsl:value-of select="PropDesc/@F_FONT_NAME" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_FONT_UNDERLINE=<xsl:value-of select="PropDesc/@F_FONT_UNDERLINE" /><xsl:text>&#10;</xsl:text>
<xsl:choose>
            <xsl:when test="PropDesc/@F_TEXT_BACKMODE='1'">
TRANSPARENT = 1
</xsl:when>
            <xsl:otherwise>
TRANSPARENT = 0
</xsl:otherwise>
         </xsl:choose><xsl:text>&#10;</xsl:text>
         <xsl:apply-templates />
      </xsl:for-each>
      <!-- Freehand-->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_CLASSNAME='Pen']">
[FREEHAND]
XOFFSET = <xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
YOFFSET = <xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
WIDTH = <xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
HEIGHT = <xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
COLOR = <xsl:value-of select="PropDesc/@F_LINE_COLOR" /><xsl:text>&#10;</xsl:text>
LINEWIDTH = <xsl:value-of select="PropDesc/@F_LINE_WIDTH" /><xsl:text>&#10;</xsl:text>
TOOLTIP = <xsl:value-of select="PropDesc/@F_ORDINAL" /> &lt;type> (<xsl:value-of select="PropDesc/@F_NAME" />)<xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_HEIGHT=<xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_WIDTH=<xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LINE_BACKMODE=<xsl:value-of select="PropDesc/@F_LINE_BACKMODE" /><xsl:text>&#10;</xsl:text>
F_POINTS = <xsl:value-of select="./PropDesc/F_POINTS" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LINE_STYLE=<xsl:value-of select="PropDesc/@F_LINE_STYLE" /><xsl:text>&#10;</xsl:text>
         <xsl:apply-templates />
      </xsl:for-each>
      <!-- Line-->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_SUBCLASS='v1-Line']">
[LINE]
X1 = <xsl:value-of select="PropDesc/@F_LINE_START_X" /><xsl:text>&#10;</xsl:text>
X2 = <xsl:value-of select="PropDesc/@F_LINE_END_X" /><xsl:text>&#10;</xsl:text>
Y1 = <xsl:value-of select="PropDesc/@F_LINE_START_Y" /><xsl:text>&#10;</xsl:text>
Y2 = <xsl:value-of select="PropDesc/@F_LINE_END_Y" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LINE_START_X=<xsl:value-of select="PropDesc/@F_LINE_START_X" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LINE_END_X=<xsl:value-of select="PropDesc/@F_LINE_END_X" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LINE_START_Y=<xsl:value-of select="PropDesc/@F_LINE_START_Y" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LINE_END_Y=<xsl:value-of select="PropDesc/@F_LINE_END_Y" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_HEIGHT=<xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_WIDTH=<xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
TOOLTIP = <xsl:value-of select="PropDesc/@F_ORDINAL" /> &lt;type> (<xsl:value-of select="PropDesc/@F_NAME" />)<xsl:text>&#10;</xsl:text>
COLOR = <xsl:value-of select="PropDesc/@F_LINE_COLOR" /><xsl:text>&#10;</xsl:text>
LINEWIDTH = <xsl:value-of select="PropDesc/@F_LINE_WIDTH" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LINE_BACKMODE=<xsl:value-of select="PropDesc/@F_LINE_BACKMODE" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LINE_STYLE=<xsl:value-of select="PropDesc/@F_LINE_STYLE" /><xsl:text>&#10;</xsl:text>
         <xsl:apply-templates />
      </xsl:for-each>
      <!-- Stamp-->
      <!-- FileNET Stamp types closely resemble ViewONE TEXT types -->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_CLASSNAME='Stamp']">
[TEXT]
CUSTOMPROPERTY = F_CLASSNAME=Stamp
FNSTAMP = 1
X = <xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
Y = <xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
WIDTH = <xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
HEIGHT = <xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_WIDTH=<xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_HEIGHT=<xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
COLOR = <xsl:value-of select="PropDesc/@F_FORECOLOR" /><xsl:text>&#10;</xsl:text>
FILLCOLOR = <xsl:value-of select="PropDesc/@F_BACKCOLOR" /><xsl:text>&#10;</xsl:text>
TEXT = <xsl:call-template name="substitute">
            <xsl:with-param name="string" select="PropDesc/F_TEXT" />
            <xsl:with-param name="from" select="'&#xA;'" />
            <xsl:with-param name="to" select="'&lt;n&gt;'" />
         </xsl:call-template><xsl:text>&#10;</xsl:text>
FONTHEIGHT = <xsl:value-of select="PropDesc/@F_FONT_SIZE" /><xsl:text>&#10;</xsl:text>
TEXTROTATION = <xsl:value-of select="PropDesc/@F_ROTATION" /><xsl:text>&#10;</xsl:text>
ROTATION = <xsl:value-of select="PropDesc/@F_IMAGEROTATION" /><xsl:text>&#10;</xsl:text>
TOOLTIP = <xsl:value-of select="PropDesc/@F_ORDINAL" /> &lt;type> (<xsl:value-of select="PropDesc/@F_NAME" />)<xsl:text>&#10;</xsl:text>
<xsl:choose>
<xsl:when test="PropDesc/@F_FONT_STRIKETHROUGH='true'">STRIKETHROUGH = 1</xsl:when>
</xsl:choose><xsl:text>&#10;</xsl:text>

CUSTOMPROPERTY = F_BORDER_BACKMODE=<xsl:value-of select="PropDesc/@F_BORDER_BACKMODE" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_BORDER_STYLE=<xsl:value-of select="PropDesc/@F_BORDER_STYLE" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_FONT_BOLD=<xsl:value-of select="PropDesc/@F_FONT_BOLD" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_FONT_ITALIC=<xsl:value-of select="PropDesc/@F_FONT_ITALIC" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_FONT_NAME=<xsl:value-of select="PropDesc/@F_FONT_NAME" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_FONT_UNDERLINE=<xsl:value-of select="PropDesc/@F_FONT_UNDERLINE" /><xsl:text>&#10;</xsl:text>
         <xsl:choose>
            <xsl:when test="PropDesc/@F_TEXT_BACKMODE='1'">
TRANSPARENT = 1
</xsl:when>
            <xsl:otherwise>
TRANSPARENT = 0
</xsl:otherwise>
         </xsl:choose><xsl:text>&#10;</xsl:text>
BORDER = <xsl:choose>
            <xsl:when test="PropDesc/@F_HASBORDER='true'">1</xsl:when>
            <xsl:otherwise>0</xsl:otherwise>
         </xsl:choose><xsl:text>&#10;</xsl:text>
BORDERCOLOR = <xsl:value-of select="PropDesc/@F_BORDER_COLOR" /><xsl:text>&#10;</xsl:text>
BORDERWIDTH = <xsl:value-of select="PropDesc/@F_BORDER_WIDTH" /><xsl:text>&#10;</xsl:text>
         <xsl:apply-templates />
      </xsl:for-each>
      <!-- Arrow-->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_CLASSNAME='Arrow']">
[ARROW]
X1 = <xsl:value-of select="PropDesc/@F_LINE_START_X" /><xsl:text>&#10;</xsl:text>
X2 = <xsl:value-of select="PropDesc/@F_LINE_END_X" /><xsl:text>&#10;</xsl:text>
Y1 = <xsl:value-of select="PropDesc/@F_LINE_START_Y" /><xsl:text>&#10;</xsl:text>
Y2 = <xsl:value-of select="PropDesc/@F_LINE_END_Y" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LINE_START_X=<xsl:value-of select="PropDesc/@F_LINE_START_X" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LINE_END_X=<xsl:value-of select="PropDesc/@F_LINE_END_X" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LINE_START_Y=<xsl:value-of select="PropDesc/@F_LINE_START_Y" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LINE_END_Y=<xsl:value-of select="PropDesc/@F_LINE_END_Y" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_HEIGHT=<xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_WIDTH=<xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
TOOLTIP = <xsl:value-of select="PropDesc/@F_ORDINAL" /> &lt;type> (<xsl:value-of select="PropDesc/@F_NAME" />)<xsl:text>&#10;</xsl:text>
COLOR = <xsl:value-of select="PropDesc/@F_LINE_COLOR" /><xsl:text>&#10;</xsl:text>
LINEWIDTH = <xsl:value-of select="PropDesc/@F_LINE_WIDTH" /><xsl:text>&#10;</xsl:text>
ARROWHEADSIZE = <xsl:value-of select="PropDesc/@F_ARROWHEAD_SIZE" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LINE_BACKMODE=<xsl:value-of select="PropDesc/@F_LINE_BACKMODE" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LINE_STYLE=<xsl:value-of select="PropDesc/@F_LINE_STYLE" /><xsl:text>&#10;</xsl:text>

         <xsl:apply-templates />
      </xsl:for-each>
      <!-- Polygon-->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_SUBCLASS='v1-Polygon']">

[POLYGON]
WIDTH = <xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
HEIGHT = <xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
COLOR = <xsl:value-of select="PropDesc/@F_LINE_COLOR" /><xsl:text>&#10;</xsl:text>
FILLCOLOR = <xsl:value-of select="PropDesc/@F_BRUSHCOLOR" /><xsl:text>&#10;</xsl:text>
LINEWIDTH = <xsl:value-of select="PropDesc/@F_LINE_WIDTH" /><xsl:text>&#10;</xsl:text>
TOOLTIP = <xsl:value-of select="PropDesc/@F_ORDINAL" /> &lt;type> (<xsl:value-of select="PropDesc/@F_NAME" />)<xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_HEIGHT=<xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_WIDTH=<xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
         <xsl:choose>
            <xsl:when test="PropDesc/@F_TEXT_BACKMODE='1'">
TRANSPARENT = 1
</xsl:when>
            <xsl:otherwise>
TRANSPARENT = 0
</xsl:otherwise>
         </xsl:choose><xsl:text>&#10;</xsl:text>

F_POINTS = <xsl:value-of select="./PropDesc/F_POINTS" /><xsl:text>&#10;</xsl:text>
         <xsl:apply-templates />
      </xsl:for-each>
      <!--Highlight Polygon-->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_SUBCLASS='v1-Highlight Polygon']">
[HIGHLIGHTPOLYGON]
COLOR = <xsl:value-of select="PropDesc/@F_LINE_COLOR" /><xsl:text>&#10;</xsl:text>
FILLCOLOR = <xsl:value-of select="PropDesc/@F_BRUSHCOLOR" /><xsl:text>&#10;</xsl:text>
LINEWIDTH = <xsl:value-of select="PropDesc/@F_LINE_WIDTH" /><xsl:text>&#10;</xsl:text>
TOOLTIP = <xsl:value-of select="PropDesc/@F_ORDINAL" /> &lt;type> (<xsl:value-of select="PropDesc/@F_NAME" />)<xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_HEIGHT=<xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_WIDTH=<xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
         <xsl:choose>
            <xsl:when test="PropDesc/@F_TEXT_BACKMODE='1'">
TRANSPARENT = 1
</xsl:when>
            <xsl:otherwise>
TRANSPARENT = 0
</xsl:otherwise>
         </xsl:choose><xsl:text>&#10;</xsl:text>
COLOR = <xsl:value-of select="PropDesc/@F_LINE_COLOR" /><xsl:text>&#10;</xsl:text>
F_POINTS = <xsl:value-of select="./PropDesc/F_POINTS" /><xsl:text>&#10;</xsl:text>
         <xsl:apply-templates />
      </xsl:for-each>
      <!--Redact Polygon-->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_SUBCLASS='v1-Redaction Polygon']">
[REDACTPOLYGON]
COLOR = <xsl:value-of select="PropDesc/@F_LINE_COLOR" /><xsl:text>&#10;</xsl:text>
FILLCOLOR = <xsl:value-of select="PropDesc/@F_BRUSHCOLOR" /><xsl:text>&#10;</xsl:text>
LINEWIDTH = <xsl:value-of select="PropDesc/@F_LINE_WIDTH" /><xsl:text>&#10;</xsl:text>
TOOLTIP = <xsl:value-of select="PropDesc/@F_ORDINAL" /> &lt;type> (<xsl:value-of select="PropDesc/@F_NAME" />)<xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_HEIGHT=<xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_WIDTH=<xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
TRANSPARENT = 0
F_POINTS = <xsl:value-of select="./PropDesc/F_POINTS" />
         <xsl:apply-templates />
      </xsl:for-each>
      <!--Open Polygon-->
      <xsl:for-each select="FnDocAnnoList/FnPageAnnoList/FnAnno[PropDesc/@F_SUBCLASS='v1-Open Polygon']">
[OPENPOLYGON]
COLOR = <xsl:value-of select="PropDesc/@F_LINE_COLOR" /><xsl:text>&#10;</xsl:text>
LINEWIDTH = <xsl:value-of select="PropDesc/@F_LINE_WIDTH" /><xsl:text>&#10;</xsl:text>
TOOLTIP = <xsl:value-of select="PropDesc/@F_ORDINAL" /> &lt;type> (<xsl:value-of select="PropDesc/@F_NAME" />)<xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_LEFT=<xsl:value-of select="PropDesc/@F_LEFT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_TOP=<xsl:value-of select="PropDesc/@F_TOP" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_HEIGHT=<xsl:value-of select="PropDesc/@F_HEIGHT" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_WIDTH=<xsl:value-of select="PropDesc/@F_WIDTH" /><xsl:text>&#10;</xsl:text>
F_POINTS = <xsl:value-of select="./PropDesc/F_POINTS" /><xsl:text>&#10;</xsl:text>
         <xsl:apply-templates />
      </xsl:for-each>
   </xsl:template>
   <!-- Common Properties -->
   <xsl:template match="PropDesc">
      <!--PAGE = <xsl:value-of select="@F_PAGENUMBER"/>-->
      <xsl:choose>
         <xsl:when test="@F_MULTIPAGETIFFPAGENUMBER &gt; 0">
PAGE = <xsl:value-of select="@F_MULTIPAGETIFFPAGENUMBER" /><xsl:text>&#10;</xsl:text>
         </xsl:when>
         <xsl:otherwise>
PAGE = <xsl:value-of select="@F_PAGENUMBER" /><xsl:text>&#10;</xsl:text>
         </xsl:otherwise>
      </xsl:choose>
CREATEDATE = <xsl:call-template name="dateTime">
         <xsl:with-param name="dateString" select="@F_ENTRYDATE" />
      </xsl:call-template><xsl:text>&#10;</xsl:text>
<xsl:if test="@F_CREATOR">CREATEDID = <xsl:value-of select="@F_CREATOR" /><xsl:text>&#10;</xsl:text></xsl:if>
MODIFIEDDATE = <xsl:call-template name="dateTime">
         <xsl:with-param name="dateString" select="@F_MODIFYDATE" />
      </xsl:call-template><xsl:text>&#10;</xsl:text>
LABEL = &lt;TYPE&gt; <xsl:value-of select="@F_NAME" /><xsl:text>&#10;</xsl:text>
FNID = <xsl:value-of select="@F_ID" /><xsl:text>&#10;</xsl:text>
FNANNOTATEDID=  <xsl:value-of select="@F_ANNOTATEDID" /><xsl:text>&#10;</xsl:text>
CUSTOMBYTES = <xsl:value-of select="./F_CUSTOM_BYTES" /><xsl:text>&#10;</xsl:text>
CUSTOMPROPERTY = F_ORDINAL=<xsl:value-of select="@F_ORDINAL" /><xsl:text>&#10;</xsl:text>
<xsl:if test="@F_CREATOR">CUSTOMPROPERTY = F_CREATOR=<xsl:value-of select="@F_CREATOR" /><xsl:text>&#10;</xsl:text></xsl:if>



   </xsl:template>


   <!-- Recursive functions to allow tokenizing of F_POINTS field -->
   <xsl:template name="points_x">
      <xsl:param name="list" />
      <xsl:param name="count" />
      <xsl:choose>
         <xsl:when test="starts-with($list,' ')">
            <xsl:call-template name="points_x">
               <xsl:with-param name="list" select="substring($list,2)" />
               <xsl:with-param name="count" select="$count" />
            </xsl:call-template>
         </xsl:when>
         <xsl:when test="contains($list,' ')">
X<xsl:value-of select="$count" /> = <xsl:value-of select="substring-before($list,' ')" />
            <xsl:call-template name="points_y">
               <xsl:with-param name="list" select="substring-after($list,' ')" />
               <xsl:with-param name="count" select="$count" />
            </xsl:call-template>
         </xsl:when>
         <xsl:otherwise>
X<xsl:value-of select="$count" /> = <xsl:value-of select="$list" />
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>


   <!-- Recursive functions to allow tokenizing of F_POINTS field -->
   <xsl:template name="points_y">
      <xsl:param name="list" />
      <xsl:param name="count" />
      <xsl:choose>
         <xsl:when test="starts-with($list,' ')">
            <xsl:call-template name="points_y">
               <xsl:with-param name="list" select="substring($list,2)" />
               <xsl:with-param name="count" select="$count" />
            </xsl:call-template>
         </xsl:when>
         <xsl:when test="contains($list,' ')">
Y<xsl:value-of select="$count" /> = <xsl:value-of select="substring-before($list,' ')" />
            <xsl:call-template name="points_x">
               <xsl:with-param name="list" select="substring-after($list,' ')" />
               <xsl:with-param name="count" select="$count+1" />
            </xsl:call-template>
         </xsl:when>
         <xsl:otherwise>
Y<xsl:value-of select="$count" /> = <xsl:value-of select="$list" />
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>


   <!-- Date Formatting-->
   <xsl:template name="dateTime">
      <xsl:param name="dateString" />
      <xsl:value-of select="substring($dateString,9,2)" />
      <xsl:text> </xsl:text>
      <xsl:call-template name="month-format">
         <xsl:with-param name="month" select="substring($dateString,6,2)" />
      </xsl:call-template>
      <xsl:text> </xsl:text>
      <xsl:value-of select="substring-before($dateString,'-')" />
      <xsl:text>, </xsl:text>
      <xsl:call-template name="time-format">
         <xsl:with-param name="timeString" select="substring-after($dateString,'T')" />
      </xsl:call-template>
   </xsl:template>

   <!-- Translate the month to text-->
   <xsl:template name="month-format">
      <xsl:param name="month" />
      <xsl:choose>
         <xsl:when test="$month='01'">Jan</xsl:when>
         <xsl:when test="$month='02'">Feb</xsl:when>
         <xsl:when test="$month='03'">Mar</xsl:when>
         <xsl:when test="$month='04'">Apr</xsl:when>
         <xsl:when test="$month='05'">May</xsl:when>
         <xsl:when test="$month='06'">Jun</xsl:when>
         <xsl:when test="$month='07'">Jul</xsl:when>
         <xsl:when test="$month='08'">Aug</xsl:when>
         <xsl:when test="$month='09'">Sep</xsl:when>
         <xsl:when test="$month='10'">Oct</xsl:when>
         <xsl:when test="$month='11'">Nov</xsl:when>
         <xsl:when test="$month='12'">Dec</xsl:when>
      </xsl:choose>
   </xsl:template>

   <!--Format the time part of the date/time-->
   <xsl:template name="time-format">
      <xsl:param name="timeString" />
      <xsl:value-of select="substring($timeString,1,8)" />
      <xsl:text>, </xsl:text>
      <xsl:choose>
         <xsl:when test="contains($timeString,'Z')">GMT</xsl:when>
         <xsl:when test="contains($timeString,'+')">
            <xsl:text> +</xsl:text>
            <xsl:value-of select="substring-after($timeString,'+')" />
         </xsl:when>
         <xsl:when test="contains($timeString,'-')">
            <xsl:text> -</xsl:text>
            <xsl:value-of select="substring-after($timeString,'-')" />
         </xsl:when>
      </xsl:choose>
   </xsl:template>

   <!-- Replace characters -->
   <xsl:template name="substitute">
      <xsl:param name="string" />
      <xsl:param name="from" select="'&amp;'" />
      <xsl:param name="to" />
      <xsl:choose>
         <xsl:when test="contains($string, $from)">
            <xsl:value-of select="substring-before($string, $from)" />
            <xsl:copy-of select="$to" />
            <xsl:call-template name="substitute">
               <xsl:with-param name="string" select="substring-after($string, $from)" />
               <xsl:with-param name="from" select="$from" />
               <xsl:with-param name="to" select="$to" />
            </xsl:call-template>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="$string" />
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>

   <!-- Document Headers -->
<xsl:template match="FnDocAnnoList">
[DOCUMENT]
ID = <xsl:value-of select="@DocID"/><xsl:text>&#10;</xsl:text>
SYSTEMTYPE = <xsl:value-of select="//@SystemType | //securityobject/@systemtype"/><xsl:text>&#10;</xsl:text>
LIBNAME = <xsl:value-of select="@LibName"/><xsl:text>&#10;</xsl:text>

<xsl:apply-templates select="FnAnnoDefPermission" /><xsl:text>&#10;</xsl:text>
<xsl:text>&#10;</xsl:text>
</xsl:template>

<!-- Security Properties -->
<xsl:template match="FnAnno//securityobject">
SECURITYMODEL = 2<xsl:text>&#10;</xsl:text>
<xsl:apply-templates select="@clientpermission" />
<xsl:apply-templates />
</xsl:template>

<xsl:template match="@clientpermission">
FNCLIENTPERMISSION = <xsl:value-of select="." /><xsl:text>&#10;</xsl:text>
</xsl:template>
<!-- IS Permissions IS Permissions IS Permissions IS Permissions IS -->
<xsl:template match="permission[@level='read']">
FNREAD = <xsl:value-of select="@name" /><xsl:text>&#10;</xsl:text>
FNREADTYPE = <xsl:value-of select="@type" /><xsl:text>&#10;</xsl:text>
<xsl:text>&#10;</xsl:text>
</xsl:template>
<xsl:template match="permission[@level='write']">
FNWRITE = <xsl:value-of select="@name" /><xsl:text>&#10;</xsl:text>
FNWRITETYPE = <xsl:value-of select="@type" /><xsl:text>&#10;</xsl:text>
<xsl:text>&#10;</xsl:text>
</xsl:template>
<xsl:template match="permission[@level='append']">
FNAPPEND = <xsl:value-of select="@name" /><xsl:text>&#10;</xsl:text>
FNAPPENDTYPE = <xsl:value-of select="@type" /><xsl:text>&#10;</xsl:text>
</xsl:template>

<!-- CS Permissions CS Permissions CS Permissions CS Permissions CS Permissions -->
<xsl:template match="FnAnno//permission[@level='none' or @level='viewer' or @level='author' or @level='owner' or @level='admin'] ">
FNPERMISSIONID = <xsl:value-of select="@id"/><xsl:text>&#10;</xsl:text>
FNPERMISSIONNAME = <xsl:value-of select="@name"/><xsl:text>&#10;</xsl:text>
FNPERMISSIONTYPE = <xsl:value-of select="@type"/><xsl:text>&#10;</xsl:text>
FNPERMISSIONLEVEL = <xsl:value-of select="@level"/><xsl:text>&#10;</xsl:text>
<xsl:text>&#10;</xsl:text></xsl:template>

<!-- Default Security Properties 
<xsl:template match="FnAnnoDefPermission">
FNDEFAULTCLIENTPERMISSION = <xsl:value-of select="security/securityobject/@clientpermission"/><xsl:text>&#10;</xsl:text>
<xsl:apply-templates/>
</xsl:template>

<xsl:template match="FnAnnoDefPermission/security/securityobject/permission[@level='read']">
FNDEFAULTREAD = <xsl:value-of select="@name"/><xsl:text>&#10;</xsl:text>
FNDEFAULTREADTYPE = <xsl:value-of select="@type"/><xsl:text>&#10;</xsl:text>
</xsl:template>

<xsl:template match="FnAnnoDefPermission/security/securityobject/permission[@level='write']">
FNDEFAULTWRITE = <xsl:value-of select="@name"/><xsl:text>&#10;</xsl:text>
FNDEFAULTWRITETYPE = <xsl:value-of select="@type"/><xsl:text>&#10;</xsl:text>
</xsl:template>

<xsl:template match="FnAnnoDefPermission/security/securityobject/permission[@level='append']">
FNDEFAULTAPPEND = <xsl:value-of select="@name"/><xsl:text>&#10;</xsl:text>
FNDEFAULTAPPENDTYPE = <xsl:value-of select="@type"/><xsl:text>&#10;</xsl:text>
</xsl:template>-->

<xsl:template match="FnAnnoDefPermission//permission[@level='none' or @level='viewer' or @level='author' or @level='owner' or @level='admin'] ">

FNDEFAULTPERMISSIONID = <xsl:value-of select="@id"/><xsl:text>&#10;</xsl:text>
FNDEFAULTPERMISSIONNAME = <xsl:value-of select="@name"/><xsl:text>&#10;</xsl:text>
FNDEFAULTPERMISSIONTYPE = <xsl:value-of select="@type"/><xsl:text>&#10;</xsl:text>
FNDEFAULTPERMISSIONLEVEL = <xsl:value-of select="@level"/><xsl:text>&#10;</xsl:text>
</xsl:template>


<!-- CE Permissions CE Permissions CE Permissions CE Permissions CE Permissions -->
<xsl:template match="permission[@level='view']">
FNEDIT = 0
FNEDITSECURITY = 0
FNDELETE = 0
<xsl:text>&#10;</xsl:text>
</xsl:template>
<xsl:template match="permission[@level='edit']">
FNEDIT = 1
FNEDITSECURITY = 0
FNDELETE = 0
FNEDIT = <xsl:value-of select="@name" />
<xsl:text>&#10;</xsl:text>
</xsl:template>
<xsl:template match="permission[@level='editsecurity']">
FNEDIT = 1
FNEDITSECURITY = 1
FNDELETE = 0
FNEDITSECURITY = <xsl:value-of select="@name" />
<xsl:text>&#10;</xsl:text>
</xsl:template>
<xsl:template match="permission[@level='delete']">
FNEDIT = 1
FNEDITSECURITY = 1
FNDELETE = 1
FNDELETE = <xsl:value-of select="@name" />
<xsl:text>&#10;</xsl:text>
</xsl:template>
<!-- 
<xsl:template match="FnAnnoDefPermission">
FNDEFAULTCLIENTPERMISSION = <xsl:value-of select="security/securityobject/@clientpermission" /><xsl:text>&#10;</xsl:text>
<xsl:apply-templates />
</xsl:template>
<xsl:template match="FnAnnoDefPermission/security/securityobject/permission[@level='read']">
FNDEFAULTREAD = <xsl:value-of select="@name" /><xsl:text>&#10;</xsl:text>
FNDEFAULTREADTYPE = <xsl:value-of select="@type" /><xsl:text>&#10;</xsl:text>
</xsl:template>
<xsl:template match="FnAnnoDefPermission/security/securityobject/permission[@level='write']">
FNDEFAULTWRITE = <xsl:value-of select="@name" /><xsl:text>&#10;</xsl:text>
FNDEFAULTWRITETYPE = <xsl:value-of select="@type" /><xsl:text>&#10;</xsl:text>
</xsl:template>
<xsl:template match="FnAnnoDefPermission/security/securityobject/permission[@level='append']">
FNDEFAULTAPPEND = <xsl:value-of select="@name" /><xsl:text>&#10;</xsl:text>
FNDEFAULTAPPENDTYPE = <xsl:value-of select="@type" /><xsl:text>&#10;</xsl:text>
</xsl:template> -->
<xsl:template match="FnAnnoDefPermission/security/securityobject/permission[@level='edit']">
FNDEFAULTEDIT = <xsl:value-of select="@name" /><xsl:text>&#10;</xsl:text>
</xsl:template>
<xsl:template match="FnAnnoDefPermission/security/securityobject/permission[@level='editsecurity']">
FNDEFAULTEDITSECURITY = <xsl:value-of select="@name" /><xsl:text>&#10;</xsl:text>
</xsl:template>
<xsl:template match="FnAnnoDefPermission/security/securityobject/permission[@level='delete']">
FNDEFAULTDELETE = <xsl:value-of select="@name" /><xsl:text>&#10;</xsl:text>

</xsl:template>
</xsl:stylesheet>
