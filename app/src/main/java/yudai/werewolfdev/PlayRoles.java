package yudai.werewolfdev;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

public class PlayRoles extends AppCompatActivity {

    private ArrayList<String> players, roles, roles_default;
    private ArrayList<Integer> score;
    private int counter = 0, answer = -1, stealing = -1, stolen = -1;
    private boolean confirmed = false, checked = false, played = false, answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_roles);

        if (savedInstanceState != null) {
            players = savedInstanceState.getStringArrayList("players");
            roles = savedInstanceState.getStringArrayList("roles");
            roles_default = savedInstanceState.getStringArrayList("roles_default");
            score = savedInstanceState.getIntegerArrayList("score");

            counter = savedInstanceState.getInt("counter");
            answer = savedInstanceState.getInt("answer");
            stealing = savedInstanceState.getInt("stealing");
            stolen = savedInstanceState.getInt("stolen");
            confirmed = savedInstanceState.getBoolean("confirmed");
            checked = savedInstanceState.getBoolean("checked");
            played = savedInstanceState.getBoolean("played");
            answered = savedInstanceState.getBoolean("answered");
        } else {
            players = getIntent().getStringArrayListExtra("players");
            roles = getIntent().getStringArrayListExtra("roles");
            roles_default = getIntent().getStringArrayListExtra("roles_default");
            score = getIntent().getIntegerArrayListExtra("score");

            Collections.shuffle(roles);
        }
        confirmPlayer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("players", players);
        outState.putStringArrayList("roles", roles);
        outState.putStringArrayList("roles_default", roles_default);
        outState.putIntegerArrayList("score", score);

        outState.putInt("counter", counter);
        outState.putInt("answer", answer);
        outState.putInt("stealing", stealing);
        outState.putInt("stolen", stolen);
        outState.putBoolean("confirmed", confirmed);
        outState.putBoolean("checked", checked);
        outState.putBoolean("played", played);
        outState.putBoolean("answered", answered);
    }

    private void confirmPlayer() {
        if (counter < players.size()) {
            if (confirmed) {
                checkRole();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.title_confirm));
                builder.setMessage(String.format(getString(R.string.message_confirm), players.get(counter)));
                builder.setNegativeButton(getString(R.string.no), null);
                builder.setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                confirmed = true;
                            }
                        });
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (confirmed) {
                            checkRole();
                        } else {
                            confirmPlayer();
                        }
                    }
                });
                builder.create().show();
            }
        } else {
            Intent intent = new Intent(this, Conversation.class);
            intent.putStringArrayListExtra("players", players);
            intent.putStringArrayListExtra("roles", roles);
            intent.putStringArrayListExtra("roles_default", roles_default);
            intent.putIntegerArrayListExtra("score", score);
            intent.putExtra("stealing", stealing);
            intent.putExtra("stolen", stolen);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    // ImageView version
    private void checkRole() {
        if (checked) {
            playRole();
        } else {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(role2image(roles.get(counter)));
            imageView.setAdjustViewBounds(true);

            new AlertDialog.Builder(this)
                    .setView(imageView)
                    .setPositiveButton(getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    checked = true;
                                }
                            })
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (checked) {
                                playRole();
                            } else {
                                checkRole();
                            }
                        }
                    })
                    .show();
        }
    }

    /* String version
    private void checkRole() {
        if (checked) {
            playRole();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.title_check_role));
            builder.setMessage(String.format(getString(R.string.message_check_role), role2string(roles.get(counter))));
            builder.setPositiveButton(getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            checked = true;
                        }
                    });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (checked) {
                        playRole();
                    } else {
                        checkRole();
                    }
                }
            });
            builder.create().show();
        }
    }
     */

    private void playRole() {
        switch (roles.get(counter)) {
            case "werewolf":
                playWerewolf();
                break;
            case "bigwolf":
                playBigwolf();
                break;
            case "madman":
                playMadman();
                break;
            case "fortuneteller":
                playFortuneteller();
                break;
            case "thief":
                playThief();
                break;
            case "hunter":
                playHunter();
                break;
            case "hangedman":
                playHangedman();
                break;
            case "villager":
                playVillager();
                break;
        }
    }

    private void playWerewolf() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_werewolf)) // builder.setTitle("Check Werewolves");
                .setMessage(showWerewolves())
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                played = true;
                            }
                        })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (played) {
                            counter += 1;
                            confirmed = false;
                            checked = false;
                            played = false;
                            confirmPlayer();
                        } else {
                            playWerewolf();
                        }
                    }
                })
                .show();
    }

    private void playBigwolf() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_bigwolf))
                .setMessage(showWerewolves() + "\n\n" + showGraves())
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                played = true;
                            }
                        })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (played) {
                            counter += 1;
                            confirmed = false;
                            checked = false;
                            played = false;
                            confirmPlayer();
                        } else {
                            playBigwolf();
                        }
                    }
                })
                .show();
    }

    private void playMadman() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_madman))
                .setMessage(R.string.message_madman)
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                played = true;
                            }
                        })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (played) {
                            counter += 1;
                            confirmed = false;
                            checked = false;
                            played = false;
                            confirmPlayer();
                        } else {
                            playMadman();
                        }
                    }
                })
                .create().show();
    }

    private void playFortuneteller() {
        if (answered && answer != -1) {
            int index = answer;
            if (index >= counter) {
                index += 1;
            }
            tellFortune(index);
        } else {
            final ArrayList<String> items = new ArrayList<>();

            for (int i = 0; i < players.size(); i++) {
                if (i != counter) {
                    items.add(players.get(i));
                }
            }
            items.add(getString(R.string.check_graves)); // items.add("Check Graves");

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.title_fortuneteller))
                    .setSingleChoiceItems(items.toArray(new String[items.size()]), answer,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    answer = i;
                                }
                            })
                    .setPositiveButton(getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    answered = true;
                                }
                            })
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (answered && answer != -1) {
                                int index = answer;
                                if (index >= counter) {
                                    index += 1;
                                }
                                tellFortune(index);
                            } else {
                                playFortuneteller();
                            }
                        }
                    })
                    .show();
        }
    }

    private void tellFortune(int i) {
        final int index = i;
        String message;

        if (index == players.size()) {
            message = showGraves();
        } else {
            message = String.format(getString(R.string.player_is_role), players.get(index), role2string(roles.get(index))); // String.format("%s is %s.", players.get(index), roles.get(index));
        }
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_fortuneteller))
                .setMessage(message)
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                played = true;
                            }
                        })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (played) {
                            counter += 1;
                            confirmed = false;
                            checked = false;
                            answered = false;
                            played = false;
                            answer = -1;
                            confirmPlayer();
                        } else {
                            tellFortune(index);
                        }
                    }
                })
                .show();
    }

    private void playThief() {
        if (answered && answer != -1) {
            int index = answer;
            if (index >= counter) {
                index += 1;
            }
            stealing = counter;
            stolen = index;
            stealRole();
        } else {
            final ArrayList<String> items = new ArrayList<>();

            for (int i = 0; i < players.size(); i++) {
                if (i != counter) {
                    items.add(players.get(i));
                }
            }
            items.add(getString(R.string.do_nothing)); // items.add("Do Nothing");

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.title_thief))
                    .setSingleChoiceItems(items.toArray(new String[items.size()]), answer,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    answer = i;
                                }
                            })
                    .setPositiveButton(getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    answered = true;
                                }
                            })
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (answered && answer != -1) {
                                int index = answer;
                                if (index >= counter) {
                                    index += 1;
                                }
                                stealing = counter;
                                stolen = index;
                                stealRole();
                            } else {
                                playThief();
                            }
                        }
                    })
                    .show();
        }
    }

    private void stealRole() {
        String message;

        if (stolen == players.size()) {
            message = getString(R.string.nothing_done); // "Nothing Done.";
            stealing = -1;
        } else {
            message = String.format(getString(R.string.player_was_role_you_are_role), players.get(stolen), role2string(roles.get(stolen)), role2string(roles.get(stolen)));
            // String.format("%s was %s.\nYou are now %s.", players.get(stolen), roles.get(stolen), roles.get(stolen));
        }
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_thief))
                .setMessage(message)
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                played = true;
                            }
                        })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (played) {
                            counter += 1;
                            confirmed = false;
                            checked = false;
                            answered = false;
                            played = false;
                            answer = -1;
                            confirmPlayer();
                        } else {
                            stealRole();
                        }
                    }
                })
                .show();
    }

    private void playHunter() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_hunter))
                .setMessage(getString(R.string.message_hunter))
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                played = true;
                            }
                        })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (played) {
                            counter += 1;
                            confirmed = false;
                            checked = false;
                            played = false;
                            confirmPlayer();
                        } else {
                            playHunter();
                        }
                    }
                })
                .show();
    }

    private void playHangedman() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_hangedman))
                .setMessage(getString(R.string.message_hangedman))
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                played = true;
                            }
                        })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (played) {
                            counter += 1;
                            confirmed = false;
                            checked = false;
                            played = false;
                            confirmPlayer();
                        } else {
                            playHangedman();
                        }
                    }
                })
                .show();
    }

    private void playVillager() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_villager))
                .setMessage(getString(R.string.message_villager))
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                played = true;
                            }
                        })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (played) {
                            counter += 1;
                            confirmed = false;
                            checked = false;
                            played = false;
                            confirmPlayer();
                        } else {
                            playVillager();
                        }
                    }
                })
                .show();
    }

    private String showWerewolves() {
        String result = "";
        for (int i = 0; i < players.size(); i++) {
            if (i != counter && roles.get(i).equals("werewolf")) {
                if (result.length() != 0) {
                    result += "\n";
                }
                result += String.format(getString(R.string.player_is_werewolf), players.get(i));
            }
            if (i != counter && roles.get(i).equals("bigwolf")) {
                if (result.length() != 0) {
                    result += "\n";
                }
                result += String.format(getString(R.string.player_is_bigwolf), players.get(i));
            }
        }
        if (result.length() == 0) {
            return getString(R.string.alone_werewolf);
        }
        return result;
    }

    private String showGraves() {
        return getString(R.string.in_grave) + "\n" + role2string(roles.get(players.size())) + "\n" + role2string(roles.get(players.size() + 1));
    }

    private String role2string(String role) {
        switch (role) {
            case "werewolf":
                return getString(R.string.werewolf);
            case "bigwolf":
                return getString(R.string.bigwolf);
            case "madman":
                return getString(R.string.madman);
            case "fortuneteller":
                return getString(R.string.fortuneteller);
            case "thief":
                return getString(R.string.thief);
            case "hunter":
                return getString(R.string.hunter);
            case "hangedman":
                return getString(R.string.hangedman);
            case "villager":
                return getString(R.string.villager);
            default:
                return "Error!";
        }
    }

    private int role2image(String role) {
        switch (role) {
            case "werewolf":
                return R.drawable.werewolf;
            case "bigwolf":
                return R.drawable.bigwolf;
            case "madman":
                return R.drawable.madman;
            case "fortuneteller":
                return R.drawable.fortuneteller;
            case "thief":
                return R.drawable.thief;
            case "hunter":
                return R.drawable.hunter;
            case "hangedman":
                return R.drawable.hangedman;
            case "villager":
                return R.drawable.villager;
            default:
                return 0;
        }
    }
}

