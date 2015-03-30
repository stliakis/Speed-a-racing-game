package tools;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Mesh.VertexDataType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.NumberUtils;

/**
 * <p>
 * A SpriteBatch is used to draw 2D rectangles that reference a texture
 * (region). The class will batch the drawing commands and optimize them for
 * processing by the GPU.
 * </p>
 * 
 * <p>
 * To draw something with a SpriteBatch one has to first call the
 * {@link SpriteBatch#begin()} method which will setup appropriate render
 * states. When you are done with drawing you have to call
 * {@link SpriteBatch#end()} which will actually draw the things you specified.
 * </p>
 * 
 * <p>
 * All drawing commands of the SpriteBatch operate in screen coordinates. The
 * screen coordinate system has an x-axis pointing to the right, an y-axis
 * pointing upwards and the origin is in the lower left corner of the screen.
 * You can also provide your own transformation and projection matrices if you
 * so wish.
 * </p>
 * 
 * <p>
 * A SpriteBatch is managed. In case the OpenGL context is lost all OpenGL
 * resources a SpriteBatch uses internally get invalidated. A context is lost
 * when a user switches to another application or receives an incoming call on
 * Android. A SpriteBatch will be automatically reloaded after the OpenGL
 * context is restored.
 * </p>
 * 
 * <p>
 * A SpriteBatch is a pretty heavy object so you should only ever have one in
 * your program.
 * </p>
 * 
 * <p>
 * A SpriteBatch works with OpenGL ES 1.x and 2.0. In the case of a 2.0 context
 * it will use its own custom shader to draw all provided sprites. You can set
 * your own custom shader via {@link #setShader(ShaderProgram)}.
 * </p>
 * 
 * <p>
 * A SpriteBatch has to be disposed if it is no longer used.
 * </p>
 * 
 * @author mzechner
 */
public class SpriteBatch implements Disposable {
	static public final int C1 = 2;
	static public final int C2 = 7;

	static public final int C3 = 12;
	static public final int C4 = 17;
	static public final int U1 = 3;

	static public final int U2 = 8;
	static public final int U3 = 13;
	static public final int U4 = 18;

	static public final int V1 = 4;
	static public final int V2 = 9;
	static public final int V3 = 14;

	static public final int V4 = 19;

	static public final int X1 = 0;
	static public final int X2 = 5;
	static public final int X3 = 10;

	static public final int X4 = 15;
	static public final int Y1 = 1;

	static public final int Y2 = 6;
	static public final int Y3 = 11;

	static public final int Y4 = 16;

	/**
	 * Returns a new instance of the default shader used by SpriteBatch for GL2
	 * when no shader is specified.
	 */
	static public ShaderProgram createDefaultShader() {
		String vertexShader = "attribute vec4 "
				+ ShaderProgram.POSITION_ATTRIBUTE
				+ ";\n" //
				+ "attribute vec4 "
				+ ShaderProgram.COLOR_ATTRIBUTE
				+ ";\n" //
				+ "attribute vec2 "
				+ ShaderProgram.TEXCOORD_ATTRIBUTE
				+ "0;\n" //
				+ "uniform mat4 u_projectionViewMatrix;\n" //
				+ "varying vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "\n" //
				+ "void main()\n" //
				+ "{\n" //
				+ "   v_color = "
				+ ShaderProgram.COLOR_ATTRIBUTE
				+ ";\n" //
				+ "   v_texCoords = "
				+ ShaderProgram.TEXCOORD_ATTRIBUTE
				+ "0;\n" //
				+ "   gl_Position =  u_projectionViewMatrix * "
				+ ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "}\n";
		String fragmentShader = "#ifdef GL_ES\n" //
				+ "#define LOWP lowp\n" //
				+ "precision mediump float;\n" //
				+ "#else\n" //
				+ "#define LOWP \n" //
				+ "#endif\n" //
				+ "varying LOWP vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "uniform sampler2D u_texture;\n" //
				+ "void main()\n"//
				+ "{\n" //
				+ "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
				+ "}";

		ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
		if (shader.isCompiled() == false) {
			throw new IllegalArgumentException("couldn't compile shader: "
					+ shader.getLog());
		}
		return shader;
	}

	private int blendDstFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;
	private boolean blendingDisabled = false;

	private int blendSrcFunc = GL20.GL_SRC_ALPHA;

	private Mesh[] buffers;

	float color = Color.WHITE.toFloatBits();

	private final Matrix4 combinedMatrix = new Matrix4();

	private int currBufferIdx = 0;

	private ShaderProgram customShader = null;

	private boolean drawing = false;

	private int idx = 0;

	private float invTexHeight = 0;

	private float invTexWidth = 0;

	private Texture lastTexture = null;

	/** the maximum number of sprites rendered in one batch so far **/
	public int maxSpritesInBatch = 0;

	private Mesh mesh;

	private boolean ownsShader;

	private final Matrix4 projectionMatrix = new Matrix4();

	/** number of render calls since last {@link #begin()} **/
	public int renderCalls = 0;

	private final ShaderProgram shader;

	private Color tempColor = new Color(1, 1, 1, 1);

	/**
	 * number of rendering calls ever, will not be reset, unless it's done
	 * manually
	 **/
	public int totalRenderCalls = 0;

	private final Matrix4 transformMatrix = new Matrix4();

	private final float[] vertices;

	/**
	 * Constructs a new SpriteBatch. Sets the projection matrix to an
	 * orthographic projection with y-axis point upwards, x-axis point to the
	 * right and the origin being in the bottom left corner of the screen. The
	 * projection will be pixel perfect with respect to the screen resolution.
	 */
	public SpriteBatch() {
		this(1000);
	}

	/**
	 * Constructs a SpriteBatch with the specified size and (if GL2) the default
	 * shader. See {@link #SpriteBatch(int, ShaderProgram)}.
	 */
	public SpriteBatch(int size) {
		this(size, null);
	}

	/**
	 * Constructs a SpriteBatch with the specified size and number of buffers
	 * and (if GL2) the default shader. See
	 * {@link #SpriteBatch(int, int, ShaderProgram)}.
	 */
	public SpriteBatch(int size, int buffers) {
		this(size, buffers, null);
	}

	/**
	 * <p>
	 * Constructs a new SpriteBatch. Sets the projection matrix to an
	 * orthographic projection with y-axis point upwards, x-axis point to the
	 * right and the origin being in the bottom left corner of the screen. The
	 * projection will be pixel perfect with respect to the screen resolution.
	 * </p>
	 * 
	 * <p>
	 * The size parameter specifies the maximum size of a single batch in number
	 * of sprites
	 * </p>
	 * 
	 * <p>
	 * The defaultShader specifies the shader to use. Note that the names for
	 * uniforms for this default shader are different than the ones expect for
	 * shaders set with {@link #setShader(ShaderProgram)}. See the
	 * {@link #createDefaultShader()} method.
	 * </p>
	 * 
	 * @param size
	 *            the batch size in number of sprites
	 * @param buffers
	 *            the number of buffers to use. only makes sense with VBOs. This
	 *            is an expert function.
	 * @param defaultShader
	 *            the default shader to use. This is not owned by the
	 *            SpriteBatch and must be disposed separately.
	 */
	public SpriteBatch(int size, int buffers, ShaderProgram defaultShader) {
		this.buffers = new Mesh[buffers];

		for (int i = 0; i < buffers; i++) {
			this.buffers[i] = new Mesh(VertexDataType.VertexArray, false,
					size * 4, size * 6, new VertexAttribute(Usage.Position, 3,
							ShaderProgram.POSITION_ATTRIBUTE),
					new VertexAttribute(Usage.ColorPacked, 4,
							ShaderProgram.COLOR_ATTRIBUTE),
					new VertexAttribute(Usage.TextureCoordinates, 2,
							ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
		}

		projectionMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		vertices = new float[size * 24];

		int len = size * 6;
		short[] indices = new short[len];
		short j = 0;
		for (int i = 0; i < len; i += 6, j += 4) {
			indices[i + 0] = (short) (j + 0);
			indices[i + 1] = (short) (j + 1);
			indices[i + 2] = (short) (j + 2);
			indices[i + 3] = (short) (j + 2);
			indices[i + 4] = (short) (j + 3);
			indices[i + 5] = (short) (j + 0);
		}
		for (int i = 0; i < buffers; i++) {
			this.buffers[i].setIndices(indices);
		}
		mesh = this.buffers[0];

		if (defaultShader == null) {
			shader = createDefaultShader();
			ownsShader = true;
		} else {
			shader = defaultShader;
		}
	}

	/**
	 * <p>
	 * Constructs a new SpriteBatch. Sets the projection matrix to an
	 * orthographic projection with y-axis point upwards, x-axis point to the
	 * right and the origin being in the bottom left corner of the screen. The
	 * projection will be pixel perfect with respect to the screen resolution.
	 * </p>
	 * 
	 * <p>
	 * The size parameter specifies the maximum size of a single batch in number
	 * of sprites
	 * </p>
	 * 
	 * <p>
	 * The defaultShader specifies the shader to use. Note that the names for
	 * uniforms for this default shader are different than the ones expect for
	 * shaders set with {@link #setShader(ShaderProgram)}. See the
	 * {@link #createDefaultShader()} method.
	 * </p>
	 * 
	 * @param size
	 *            the batch size in number of sprites
	 * @param defaultShader
	 *            the default shader to use. This is not owned by the
	 *            SpriteBatch and must be disposed separately.
	 */
	public SpriteBatch(int size, ShaderProgram defaultShader) {
		this(size, 1, defaultShader);
	}

	/**
	 * Sets up the SpriteBatch for drawing. This will disable depth buffer
	 * writting. It enables blending and texturing. If you have more texture
	 * units enabled than the first one you have to disable them before calling
	 * this. Uses a screen coordinate system by default where everything is
	 * given in pixels. You can specify your own projection and modelview
	 * matrices via {@link #setProjectionMatrix(Matrix4)} and
	 * {@link #setTransformMatrix(Matrix4)}.
	 */
	public void begin() {
		if (drawing) {
			throw new IllegalStateException(
					"you have to call SpriteBatch.end() first");
		}
		renderCalls = 0;

		Gdx.gl.glDepthMask(false);
		if (customShader != null) {
			customShader.begin();
		} else {
			shader.begin();
		}
		setupMatrices();

		idx = 0;
		lastTexture = null;
		drawing = true;
	}

	/**
	 * Disables blending for drawing sprites. Does not disable blending for text
	 * rendering
	 */
	public void disableBlending() {
		renderMesh();
		blendingDisabled = true;
	}

	/** Disposes all resources associated with this SpriteBatch */
	@Override
	public void dispose() {
		for (int i = 0; i < buffers.length; i++) {
			buffers[i].dispose();
		}
		if (ownsShader && shader != null) {
			shader.dispose();
		}
	}
	public void drawWithSize(Texture texture, float[] spriteVertices,int verts) {
		if (!drawing) {
			throw new IllegalStateException(
					"SpriteBatch.begin must be called before draw.");
		}

		if (texture != lastTexture) {
			switchTexture(texture);
		}

		int remainingVertices = vertices.length - idx;
		if (remainingVertices == 0) {
			renderMesh();
			remainingVertices = vertices.length;
		}

		for(int c=0;c<verts;c++){
			vertices[idx++] = spriteVertices[c*5+0];
			vertices[idx++] = spriteVertices[c*5+1];
			vertices[idx++] = 0;
			vertices[idx++] = spriteVertices[c*5+2];
			vertices[idx++] = spriteVertices[c*5+3];
			vertices[idx++] = spriteVertices[c*5+4];
		}
	}
	
	public void draw (TextureRegion region, float x, float y,float z, float width, float height) {
		if (!drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

		Texture texture = region.getTexture();
		if (texture != lastTexture) {
			switchTexture(texture);
		} else if (idx == vertices.length) //
			renderMesh();

		final float fx2 = x + width;
		final float fy2 = y + height;
		final float u = region.getU();
		final float v = region.getV2();
		final float u2 = region.getU2();
		final float v2 = region.getV();

		vertices[idx++] = x;
		vertices[idx++] = y;
		vertices[idx++] = z;
		vertices[idx++] = color;
		vertices[idx++] = u;
		vertices[idx++] = v;

		vertices[idx++] = x;
		vertices[idx++] = fy2;
		vertices[idx++] = z;
		vertices[idx++] = color;
		vertices[idx++] = u;
		vertices[idx++] = v2;

		vertices[idx++] = fx2;
		vertices[idx++] = fy2;
		vertices[idx++] = z;
		vertices[idx++] = color;
		vertices[idx++] = u2;
		vertices[idx++] = v2;

		vertices[idx++] = fx2;
		vertices[idx++] = y;
		vertices[idx++] = z;
		vertices[idx++] = color;
		vertices[idx++] = u2;
		vertices[idx++] = v;
	}
	
	public void draw(Texture texture, float[] spriteVertices, float z) {
		if (!drawing) {
			throw new IllegalStateException(
					"SpriteBatch.begin must be called before draw.");
		}

		if (texture != lastTexture) {
			switchTexture(texture);
		}

		int remainingVertices = vertices.length - idx;
		if (remainingVertices == 0) {
			renderMesh();
			remainingVertices = vertices.length;
		}

		vertices[idx++] = spriteVertices[0];
		vertices[idx++] = spriteVertices[1];
		vertices[idx++] = z;
		vertices[idx++] = spriteVertices[2];
		vertices[idx++] = spriteVertices[3];
		vertices[idx++] = spriteVertices[4];

		vertices[idx++] = spriteVertices[5];
		vertices[idx++] = spriteVertices[6];
		vertices[idx++] = z;
		vertices[idx++] = spriteVertices[7];
		vertices[idx++] = spriteVertices[8];
		vertices[idx++] = spriteVertices[9];

		vertices[idx++] = spriteVertices[10];
		vertices[idx++] = spriteVertices[11];
		vertices[idx++] = z;
		vertices[idx++] = spriteVertices[12];
		vertices[idx++] = spriteVertices[13];
		vertices[idx++] = spriteVertices[14];

		vertices[idx++] = spriteVertices[15];
		vertices[idx++] = spriteVertices[16];
		vertices[idx++] = z;
		vertices[idx++] = spriteVertices[17];
		vertices[idx++] = spriteVertices[18];
		vertices[idx++] = spriteVertices[19];
	}
	public void drawTriangles(Texture texture, float[] spriteVertices,float r,float g,float b,float a) {
		if (!drawing) {
			throw new IllegalStateException(
					"SpriteBatch.begin must be called before draw.");
		}

		if (texture != lastTexture) {
			switchTexture(texture);
		}

		int remainingVertices = vertices.length - idx;
		if (remainingVertices == 0) {
			renderMesh();
			remainingVertices = vertices.length;
		}

		vertices[idx++] = spriteVertices[0];
		vertices[idx++] = spriteVertices[1];
		vertices[idx++] = 0;
		vertices[idx++] = Color.toFloatBits(r, g, b, a);
		vertices[idx++] = spriteVertices[2];
		vertices[idx++] = spriteVertices[3];

		vertices[idx++] = spriteVertices[4];
		vertices[idx++] = spriteVertices[5];
		vertices[idx++] = 0;
		vertices[idx++] = Color.toFloatBits(r, g, b, a);
		vertices[idx++] = spriteVertices[6];
		vertices[idx++] = spriteVertices[7];

		vertices[idx++] = spriteVertices[8];
		vertices[idx++] = spriteVertices[9];
		vertices[idx++] = 0;
		vertices[idx++] = Color.toFloatBits(r, g, b, a);
		vertices[idx++] = spriteVertices[10];
		vertices[idx++] = spriteVertices[11];
		
		vertices[idx++] = spriteVertices[12];
		vertices[idx++] = spriteVertices[13];
		vertices[idx++] = 0;
		vertices[idx++] = Color.toFloatBits(r, g, b, a);
		vertices[idx++] = spriteVertices[14];
		vertices[idx++] = spriteVertices[15];

	}
	/**
	 * Draws a rectangle using the given vertices. There must be 4 vertices,
	 * each made up of 5 elements in this order: x, y, color, u, v.
	 */
	public void draw(Texture texture, float[] spriteVertices, int offset,
			int length) {
		if (!drawing) {
			throw new IllegalStateException(
					"SpriteBatch.begin must be called before draw.");
		}

		if (texture != lastTexture) {
			switchTexture(texture);
		}

		int remainingVertices = vertices.length - idx;
		if (remainingVertices == 0) {
			renderMesh();
			remainingVertices = vertices.length;
		}
		int vertexCount = Math.min(remainingVertices, length - offset);
		System.arraycopy(spriteVertices, offset, vertices, idx, vertexCount);
		offset += vertexCount;
		idx += vertexCount;

		while (offset < length) {
			renderMesh();
			vertexCount = Math.min(vertices.length, length - offset);
			System.arraycopy(spriteVertices, offset, vertices, 0, vertexCount);
			offset += vertexCount;
			idx += vertexCount;
		}
	}

	/** Enables blending for sprites */
	public void enableBlending() {
		renderMesh();
		blendingDisabled = false;
	}

	/**
	 * Finishes off rendering. Enables depth writes, disables blending and
	 * texturing. Must always be called after a call to {@link #begin()}
	 */
	public void end() {
		if (!drawing) {
			throw new IllegalStateException(
					"SpriteBatch.begin must be called before end.");
		}
		if (idx > 0) {
			renderMesh();
		}
		lastTexture = null;
		idx = 0;
		drawing = false;

		GL20 gl = Gdx.gl;
		gl.glDepthMask(true);
		if (isBlendingEnabled()) {
			gl.glDisable(GL20.GL_BLEND);
		}

		if (customShader != null) {
			customShader.end();
		} else {
			shader.end();
		}
	}

	/**
	 * Causes any pending sprites to be rendered, without ending the
	 * SpriteBatch.
	 */
	public void flush() {
		renderMesh();
	}

	/**
	 * @return the rendering color of this SpriteBatch. Manipulating the
	 *         returned instance has no effect.
	 */
	public Color getColor() {
		int intBits = NumberUtils.floatToIntColor(color);
		Color color = tempColor;
		color.r = (intBits & 0xff) / 255f;
		color.g = ((intBits >>> 8) & 0xff) / 255f;
		color.b = ((intBits >>> 16) & 0xff) / 255f;
		color.a = ((intBits >>> 24) & 0xff) / 255f;
		return color;
	}

	/**
	 * Returns the current projection matrix. Changing this will result in
	 * undefined behaviour.
	 * 
	 * @return the currently set projection matrix
	 */
	public Matrix4 getProjectionMatrix() {
		return projectionMatrix;
	}

	/**
	 * Returns the current transform matrix. Changing this will result in
	 * undefined behaviour.
	 * 
	 * @return the currently set transform matrix
	 */
	public Matrix4 getTransformMatrix() {
		return transformMatrix;
	}

	/** @return whether blending for sprites is enabled */
	public boolean isBlendingEnabled() {
		return !blendingDisabled;
	}

	private void renderMesh() {
		if (idx == 0) {
			return;
		}

		renderCalls++;
		totalRenderCalls++;
		int spritesInBatch = idx / 24;
		if (spritesInBatch > maxSpritesInBatch) {
			maxSpritesInBatch = spritesInBatch;
		}

		lastTexture.bind();
		mesh.setVertices(vertices, 0, idx);
		mesh.getIndicesBuffer().position(0);
		mesh.getIndicesBuffer().limit(spritesInBatch * 6);

		if (blendingDisabled) {
			Gdx.gl.glDisable(GL20.GL_BLEND);
		} else {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(blendSrcFunc, blendDstFunc);
		}

		if (customShader != null) {
			mesh.render(customShader, GL20.GL_TRIANGLES, 0,
					spritesInBatch * 6);
		} else {
			mesh.render(shader, GL20.GL_TRIANGLES, 0, spritesInBatch * 6);
		}

		idx = 0;
		currBufferIdx++;
		if (currBufferIdx == buffers.length) {
			currBufferIdx = 0;
		}
		mesh = buffers[currBufferIdx];
	}

	/**
	 * Sets the blending function to be used when rendering sprites.
	 * 
	 * @param srcFunc
	 *            the source function, e.g. GL20.GL_SRC_ALPHA
	 * @param dstFunc
	 *            the destination function, e.g. GL20.GL_ONE_MINUS_SRC_ALPHA
	 */
	public void setBlendFunction(int srcFunc, int dstFunc) {
		renderMesh();
		blendSrcFunc = srcFunc;
		blendDstFunc = dstFunc;
	}

	/**
	 * Sets the color used to tint images when they are added to the
	 * SpriteBatch. Default is {@link Color#WHITE}.
	 */
	public void setColor(Color tint) {
		color = tint.toFloatBits();
	}

	/**
	 * @see #setColor(Color)
	 * @see Color#toFloatBits()
	 */
	public void setColor(float color) {
		this.color = color;
	}

	/** @see #setColor(Color) */
	public void setColor(float r, float g, float b, float a) {
		int intBits = (int) (255 * a) << 24 | (int) (255 * b) << 16
				| (int) (255 * g) << 8 | (int) (255 * r);
		color = NumberUtils.intToFloatColor(intBits);
	}

	/**
	 * Sets the projection matrix to be used by this SpriteBatch. If this is
	 * called inside a {@link #begin()}/{@link #end()} block. the current batch
	 * is flushed to the gpu.
	 * 
	 * @param projection
	 *            the projection matrix
	 */
	public void setProjectionMatrix(Matrix4 projection) {
		if (drawing) {
			flush();
		}
		projectionMatrix.set(projection);
		if (drawing) {
			setupMatrices();
		}
	}

	/**
	 * Sets the shader to be used in a GLES 2.0 environment. Vertex position
	 * attribute is called "a_position", the texture coordinates attribute is
	 * called called "a_texCoords0", the color attribute is called "a_color".
	 * See {@link ShaderProgram#POSITION_ATTRIBUTE},
	 * {@link ShaderProgram#COLOR_ATTRIBUTE} and
	 * {@link ShaderProgram#TEXCOORD_ATTRIBUTE} which gets "0" appened to
	 * indicate the use of the first texture unit. The projection matrix is
	 * uploaded via a mat4 uniform called "u_proj", the transform matrix is
	 * uploaded via a uniform called "u_trans", the combined transform and
	 * projection matrx is is uploaded via a mat4 uniform called "u_projTrans".
	 * The texture sampler is passed via a uniform called "u_texture".</p>
	 * 
	 * Call this method with a null argument to use the default shader.</p>
	 * 
	 * This method will flush the batch before setting the new shader, you can
	 * call it in between {@link #begin()} and {@link #end()}.
	 * 
	 * @param shader
	 *            the {@link ShaderProgram} or null to use the default shader.
	 */
	public void setShader(ShaderProgram shader) {
		if (drawing) {
			flush();
			if (customShader != null) {
				customShader.end();
			} else {
				this.shader.end();
			}
		}
		customShader = shader;
		if (drawing) {
			if (customShader != null) {
				customShader.begin();
			} else {
				this.shader.begin();
			}
			setupMatrices();
		}
	}

	/**
	 * Sets the transform matrix to be used by this SpriteBatch. If this is
	 * called inside a {@link #begin()}/{@link #end()} block. the current batch
	 * is flushed to the gpu.
	 * 
	 * @param transform
	 *            the transform matrix
	 */
	public void setTransformMatrix(Matrix4 transform) {
		if (drawing) {
			flush();
		}
		transformMatrix.set(transform);
		if (drawing) {
			setupMatrices();
		}
	}

	private void setupMatrices() {
		combinedMatrix.set(projectionMatrix).mul(transformMatrix);
		if (customShader != null) {
			customShader.setUniformMatrix("u_proj", projectionMatrix);
			customShader.setUniformMatrix("u_trans", transformMatrix);
			customShader.setUniformMatrix("u_projTrans", combinedMatrix);
			customShader.setUniformi("u_texture", 0);
		} else {
			shader.setUniformMatrix("u_projectionViewMatrix",
					combinedMatrix);
			shader.setUniformi("u_texture", 0);
		}
	}

	private void switchTexture(Texture texture) {
			renderMesh();
			lastTexture = texture;
			invTexWidth = 1.0f / texture.getWidth();
			invTexHeight = 1.0f / texture.getHeight();
	}
}
