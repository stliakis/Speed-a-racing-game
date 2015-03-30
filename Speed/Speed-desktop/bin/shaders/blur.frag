#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

varying vec4 vertexWorldPos;
uniform vec4 backColor;

uniform float effectScale;
uniform float width;
uniform float height;
uniform vec4 blurColor;
void main() {
    vec2 tex;
    vec4 color2=vec4(0.0,0.0,0.0,1.0);
    
    float stepx=(1.0/width)*(effectScale)*4.0;
    float stepy=(1.0/height)*(effectScale)*4.0;
    vec4 color;
    
    tex.x=v_texCoords.x+stepx;
    tex.y=v_texCoords.y+stepy;
    color=v_color * texture2D(u_texture, tex);
    color2.r+=color.r;
    color2.g+=color.g;
    color2.b+=color.b;

	tex.x=v_texCoords.x-stepx;
    tex.y=v_texCoords.y+stepy;
    color=v_color * texture2D(u_texture, tex);
    color2.r+=color.r;
    color2.g+=color.g;
    color2.b+=color.b;
	
	tex.x=v_texCoords.x-stepx;
    tex.y=v_texCoords.y-stepy;
    color=v_color * texture2D(u_texture, tex);
    color2.r+=color.r;
    color2.g+=color.g;
    color2.b+=color.b;
	
	tex.x=v_texCoords.x+stepx;
    tex.y=v_texCoords.y-stepy;
    color=v_color * texture2D(u_texture, tex);
    color2.r+=color.r;
    color2.g+=color.g;
    color2.b+=color.b;
    
    tex.x=v_texCoords.x;
    tex.y=v_texCoords.y;
    color=v_color * texture2D(u_texture, tex);
    color2.r+=color.r;
    color2.g+=color.g;
    color2.b+=color.b;
	
    color2.r/=5.0;
    color2.g/=5.0;
    color2.b/=5.0;
    
	color2*=blurColor;
  	gl_FragColor = color2;
}


