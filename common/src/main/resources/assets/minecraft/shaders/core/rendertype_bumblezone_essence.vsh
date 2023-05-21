#version 150

#moj_import <projection.glsl>

in vec3 Position;
in vec4 Color;
in vec3 Normal;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat3 IViewRotMat;

out vec3 view;
out vec3 uv3d;
out vec4 texProj0;
out vec4 vertexColor;

vec4 ModelPos = vec4(Position, 1.0);
mat4 ICamJiggleMat = mat4(inverse(mat3(ProjMat))) * ProjMat;

vec3 InitialDirection = vec3(1.0, 0.0, 0.0);
vec3 crossProduct = cross(InitialDirection, Normal);

mat3 Rotate = mat3(
    1.0, -crossProduct.z, crossProduct.y,
    crossProduct.z, 1.0, -crossProduct.x,
    -crossProduct.y, crossProduct.x, 1.0
);

void main() {
    ICamJiggleMat[2].w = 0.0;
    ICamJiggleMat[3].z = 0.0;

    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    view = Rotate * IViewRotMat * (ICamJiggleMat * ModelPos).xyz;
    vertexColor = Color;
    texProj0 = projection_from_position(gl_Position);
}
