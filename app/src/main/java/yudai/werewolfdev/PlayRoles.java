package yudai.werewolfdev;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class PlayRoles extends AppCompatActivity {

    private ArrayList<String> players = new ArrayList<>(), roles = new ArrayList<>();
    private ArrayList<Integer> score = new ArrayList<>();
    private int counter = 0, answer = -1, stealing = -1, stolen = -1;
    private boolean confirmed = false, checked = false, played = false, answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_roles);

        if (savedInstanceState != null) {
            players = savedInstanceState.getStringArrayList("players");
            roles = savedInstanceState.getStringArrayList("roles");
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
            score = getIntent().getIntegerArrayListExtra("score");

            Collections.shuffle(roles);
        }
        confirmPlayer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("players", players);
        outState.putStringArrayList("roles", roles);
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
                builder.setTitle("Confirm");
                builder.setMessage(String.format("Are you really %s?", players.get(counter)));
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes",
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
            intent.putIntegerArrayListExtra("score", score);
            intent.putExtra("stealing", stealing);
            intent.putExtra("stolen", stolen);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void checkRole() {
        if (checked) {
            playRole();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Check Your Role");
            builder.setMessage(String.format("You are %s.", roles.get(counter)));
            builder.setPositiveButton("Yes",
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Check Werewolves");
        builder.setMessage(showWerewolves());
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        played = true;
                    }
                });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
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
        });
        builder.create().show();
    }

    private void playBigwolf() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Check Werewolves");
        builder.setMessage(showWerewolves() + "\n\n" + showGraves());
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        played = true;
                    }
                });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
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
        });
        builder.create().show();
    }

    private void playMadman() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Madman");
        builder.setMessage("Madman.");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        played = true;
                    }
                });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
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
        });
        builder.create().show();
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
            items.add("Check Graves");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.fortuneteller));
            builder.setSingleChoiceItems(items.toArray(new String[items.size()]), answer,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            answer = i;
                        }
                    });
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            answered = true;
                        }
                    });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
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
            });
            builder.create().show();
        }
    }

    private void tellFortune(int i) {
        final int index = i;
        String message;

        if (index == players.size()) {
            message = showGraves();
        } else {
            message = String.format("%s is %s.", players.get(index), roles.get(index));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tell Fortune");
        builder.setMessage(message);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        played = true;
                    }
                });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
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
        });
        builder.create().show();
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
            items.add("Do Nothing");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Steal Role");
            builder.setSingleChoiceItems(items.toArray(new String[items.size()]), answer,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            answer = i;
                        }
                    });
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            answered = true;
                        }
                    });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
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
            });
            builder.create().show();
        }
    }

    private void stealRole() {
        String message;

        if (stolen == players.size()) {
            message = "Nothing to be done.";
            stealing = -1;
        } else {
            message = String.format("%s was %s.\nYou are now %s.", players.get(stolen), roles.get(stolen), roles.get(stolen));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Steal Role");
        builder.setMessage(message);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        played = true;
                    }
                });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
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
        });
        builder.create().show();
    }

    private void playHunter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hunter");
        builder.setMessage("Hunter");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        played = true;
                    }
                });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
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
        });
        builder.create().show();
    }

    private void playHangedman() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hangedman");
        builder.setMessage("Hangedman");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        played = true;
                    }
                });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
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
        });
        builder.create().show();
    }

    private void playVillager() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Villager");
        builder.setMessage("Villager");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        played = true;
                    }
                });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
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
        });
        builder.create().show();
    }

    private String showWerewolves() {
        String result = "";
        for (int i = 0; i < players.size(); i++) {
            if (i != counter && roles.get(i).equals("werewolf")) {
                if (result.length() != 0) {
                    result += "\n";
                }
                result += String.format("%s is werewolf.", players.get(i));
            }
            if (i != counter && roles.get(i).equals("bigwolf")) {
                if (result.length() != 0) {
                    result += "\n";
                }
                result += String.format("%s is bigwolf.", players.get(i));
            }
        }
        if (result.length() == 0) {
            return "Alone werewolf.";
        }
        return result;
    }

    private String showGraves() {
        return "In the grave:" + "\n" + roles.get(players.size()) + "\n" + roles.get(players.size() + 1);
    }

}
