attribute vec3 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;
uniform vec4 v_color2;
varying vec4 v_color;
varying vec2 v_texCoords;

void main()
{
	v_color=v_color2;
	 v_texCoords = a_texCoord0;
	 gl_Position =  u_projTrans * vec4(a_position.xyz,1.0) ;
 }

