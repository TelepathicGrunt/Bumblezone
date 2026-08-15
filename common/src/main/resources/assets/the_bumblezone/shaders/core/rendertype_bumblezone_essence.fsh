#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:matrix.glsl>
#moj_import <minecraft:globals.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec4 texProj0;
in vec3 view;

const vec3[] COLORS = vec3[](
    vec3(0.92, 0.92, 0.92),
    vec3(0.75, 0.1, 0.1),
    vec3(0.65, 0.65, 0.1),
    vec3(0.1, 0.75, 0.1),
    vec3(0.1, 0.1, 0.75),
    vec3(0.7, 0.1, 0.6)
);

const mat4 SCALE_TRANSLATE = mat4(
    0.5, 0.0, 0.0, 0.25,
    0.0, 0.5, 0.0, 0.25,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
);

vec2 angle2vec2(float radians) {
    return vec2(cos(radians), sin(radians));
}

mat4 base_layer() {
    float time = GameTime * 20.0;
    mat4 translate = mat4(
        1.0, 0.0, 0.0, 0.0,
        0.0, 1.0, 0.0, time,
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );

    mat2 rotate = mat2(
        1.0, 0.0,
        0.0, 1.0
    );

    float scaleAmount = 3;
    mat2 scale = mat2(
        scaleAmount, 0.0,
        0.0, scaleAmount
    );

    return mat4(scale * rotate) * translate;
}

mat4 bee_layer(float layer) {
    float time = (GameTime * (30.0 + (layer * 8)));
    mat4 translate = mat4(
        1.0, 0.0, 0.0, 17.0 / layer,
        0.0, 1.0, 0.0, time,
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );

    mat2 rotate = mat2_rotate_z(radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));
    mat2 scale = mat2((4.5 - layer / 4.0) * 2.0);
    return mat4(scale * rotate) * translate * SCALE_TRANSLATE;
}

out vec4 fragColor;

void main() {
    vec3 color = textureProj(Sampler0, texProj0 * base_layer()).rgb * COLORS[0];
    for (int i = 0; i < LAYERS; i++) {
        color += textureProj(Sampler1, texProj0 * bee_layer(float(i + 1))).rgb * COLORS[i + 1];
    }
    fragColor = apply_fog(vec4(color * vertexColor.rgb, 1.0), sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
