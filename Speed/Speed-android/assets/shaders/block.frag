varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform vec4 fog_color;
void main()
{
   float perspective_far = 250.0;
   float fog_cord = (gl_FragCoord.z / gl_FragCoord.w) / perspective_far;
   float fog_density = 10.0;
   float fog = fog_cord * fog_density;
   vec4 frag_color = v_color;
   gl_FragColor = mix(fog_color, frag_color, clamp(1.0-fog,0.0, 1.0));
   gl_FragColor.a=v_color.a;
}
