module.exports = function(grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        shell: {
            options: {
                stdout: true,
                stderr: true,
            },
            runServer: {
                command: 'java -cp server.jar main.Main 8080'
            }
        },

        watch: {
            fest: {
                files: ['frontend/templates/**/*.xml'],
                tasks: ['fest'],
                options: {
                    interrupt: true,
                    livereload: true,
                }
            },
            server: {
                files: [
                    'frontend/static/**/*'
                ],
                tasks: ['copy:main'],
                options: {
                    interrupt: true,
                    livereload: true,
                }
            },
            server_tml: {
                files: [
                    'backend/server_tml/**/*'
                ],
                tasks: ['copy:server_tml'],
                options: {
                    interrupt: true,
                    livereload: false,
                }
            },
            public_html: {
                files: [
                    'public_html/**/*'
                ],
                tasks: ['copy:public_html'],
                options: {
                    interrupt: true,
                    livereload: false,
                }
            },
            ts: {
                files: [
                    'frontend/typescript/**/*'
                ],
                tasks: ['ts', 'concat:ts'],
                options: {
                    interrupt: true,
                    livereload: true,
                }
            },
        },

        fest: {
            templates: {
                files: [{
                    expand: true,
                    cwd: 'frontend/templates',
                    src: '**/*.xml',
                    dest: 'frontend/templates/bin'
                }],
                options: {
                    template: function (data) { /* задаем формат функции-шаблона */
                        return grunt.template.process(
                            'var <%= name %>Tmpl = <%= contents %> ;', /* присваиваем функцию-шаблон переменной */
                            {data: data}
                        );
                    }
                }
            }
        },

        copy: {
            server_tml: {
                expand: true,
                cwd: 'backend/server_tml/',
                src: '**',
                dest: 'server_tml/',
                flatten: false,
            },
            main: {
                expand: true,
                cwd: 'frontend/static/',
                src: '**',
                dest: 'public_html/',
                flatten: false,
            },
            public_html: {
                expand: true,
                cwd: 'public_html/',
                src: '**',
                dest: 'backend/public_html/',
                flatten: false,
            }
        },

        concurrent: {
            target: ['watch', 'shell:runServer'],
            options: {
                logConcurrentOutput: true
            },
        },

        concat: {
            options: {
                separator: ';\n',
            },
            ts: {
                src: ['frontend/typescript/js/**/*.js', 'frontend/templates/bin/**/*.js', 'frontend/typescript/bin.js'],
                dest: 'public_html/main.js',
            },
        },

        ts: {
            default : {
                src: ['frontend/typescript/main.ts'],
                out: 'frontend/typescript/bin.js',
                options: {
                    inlineSourceMap: true,
                },
            }
        },

    });


    grunt.loadNpmTasks('grunt-contrib-requirejs');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-concurrent');
    grunt.loadNpmTasks('grunt-shell-spawn');
    grunt.loadNpmTasks('grunt-fest');
    grunt.loadNpmTasks('grunt-sass');
    grunt.loadNpmTasks('grunt-concat-css');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-ts');

    grunt.registerTask('default', ['fest', 'copy', 'ts', 'concat']);
    grunt.registerTask('watcher', ['default', 'watch']);
    grunt.registerTask('test', ['default', 'concurrent']);

}